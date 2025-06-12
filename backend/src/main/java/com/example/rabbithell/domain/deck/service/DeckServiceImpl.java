package com.example.rabbithell.domain.deck.service;

import static com.example.rabbithell.domain.deck.exception.code.DeckExceptionCode.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.deck.dto.DeckRedisDto;
import com.example.rabbithell.domain.deck.dto.EffectDetailDto;
import com.example.rabbithell.domain.deck.dto.PawCardEffectDto;
import com.example.rabbithell.domain.deck.dto.request.ActivePawCardRequest;
import com.example.rabbithell.domain.deck.dto.request.BatchActivePawCardRequest;
import com.example.rabbithell.domain.deck.dto.request.DeckCond;
import com.example.rabbithell.domain.deck.dto.response.BatchActivePawCardResponse;
import com.example.rabbithell.domain.deck.dto.response.DeckResponse;
import com.example.rabbithell.domain.deck.entity.Deck;
import com.example.rabbithell.domain.deck.entity.EffectDetail;
import com.example.rabbithell.domain.deck.entity.PawCardEffect;
import com.example.rabbithell.domain.deck.enums.PawCardSlot;
import com.example.rabbithell.domain.deck.exception.DeckException;
import com.example.rabbithell.domain.deck.repository.DeckRepository;
import com.example.rabbithell.domain.deck.repository.PawCardEffectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeckServiceImpl implements DeckService {

	private final DeckRepository deckRepository;
	private final PawCardEffectRepository pawCardEffectRepository;
	private final DeckEffectProcessor deckEffectProcessor;
	private final PawCardEffectRedisService pawCardEffectRedisService;
	private final DeckRedisService deckRedisService;

	@Transactional(readOnly = true)
	@Override
	public PageResponse<DeckResponse> findAllDeckByCond(Long cloverId, int pageNumber, int size,
		DeckCond cond) {

		Pageable pageable = PageRequest.of(pageNumber - 1, size);
		List<Deck> deckList = deckRepository.findAllByCondition(cloverId, cond, pageable);
		long count = deckRepository.countByCondition(cloverId, cond);
		List<DeckResponse> responseList = deckList.stream()
			.map(DeckResponse::fromEntity)
			.toList();
		PageImpl<DeckResponse> responsePage = new PageImpl<>(responseList, pageable, count);
		return PageResponse.of(responseList, responsePage);
	}

	@Transactional(readOnly = true)
	@Override
	public DeckResponse findDeckById(Long deckId, Long cloverId) {
		Deck deck = deckRepository.findByIdAndCloverIdOrElseThrow(deckId, cloverId);
		return DeckResponse.fromEntity(deck);
	}

	@Override
	public void calculateEffect(Long cloverId) {

		// 1. 장착된 덱 정보 DB에서 조회
		List<Deck> equippedDecks = deckRepository.findEquippedByCloverId(cloverId);
		PawCardEffect effect = pawCardEffectRepository.findByCloverIdWithDetails(cloverId);

		// 2. 효과 정보 조회
		List<EffectDetail> details = effect.getDetails();
		if (details.size() < 2) {
			throw new DeckException(INVALID_EFFECT_STRUCTURE);
		}

		// 3. 효과 계산
		deckEffectProcessor.processAllEffects(equippedDecks, effect, details);

		// 4. Redis 캐싱
		PawCardEffectDto effectResponse = PawCardEffectDto.from(
			effect,
			details.stream().map(EffectDetailDto::fromEntity).toList()
		);

		pawCardEffectRedisService.saveEffect(cloverId, effectResponse);
	}

	@Override
	@Transactional
	public void assignSlots(Long cloverId, BatchActivePawCardRequest request) {

		List<ActivePawCardRequest> requestList = request.activePawCardRequestList();

		// 1. 요청을 Map<deckId, slot>으로 변환하여 조회
		Map<Long, PawCardSlot> requestedDeckMap = requestList.stream()
			.collect(Collectors.toMap(ActivePawCardRequest::deckId, ActivePawCardRequest::pawCardSlot));

		// 2. 클로버의 모든 덱을 락을 걸고 한 번에 조회
		List<Deck> allDecks = deckRepository.findAllByCloverIdWithLock(cloverId);

		// 3. 슬롯 해제 대상만 선별
		List<Deck> decksToRelease = allDecks.stream()
			.filter(deck -> {
				PawCardSlot currentSlot = deck.getPawCardSlot();
				PawCardSlot requestedSlot = requestedDeckMap.get(deck.getId());

				return currentSlot != null && (!requestedDeckMap.containsKey(deck.getId()) || !currentSlot.equals(
					requestedSlot));
			})
			.toList();

		for (Deck deck : decksToRelease) {
			deck.equipDeck(null);
		}

		// 4. 슬롯 장착 대상만 필터링해서 업데이트
		List<Deck> decksToEquip = allDecks.stream()
			.filter(deck -> requestedDeckMap.containsKey(deck.getId()))
			.toList();

		for (Deck deck : decksToEquip) {
			PawCardSlot newSlot = requestedDeckMap.get(deck.getId());
			deck.equipDeck(newSlot);
		}

		// 5. Redis 캐싱용 최종 장착 상태 조회
		List<Deck> equippedDecks = allDecks.stream()
			.filter(d -> d.getPawCardSlot() != null)
			.toList();

		List<DeckRedisDto> deckRedisDtoList = equippedDecks.stream()
			.map(DeckRedisDto::fromEntity)
			.sorted(Comparator.comparing(DeckRedisDto::getPawCardSlot))
			.toList();

		deckRedisService.saveDecks(cloverId, deckRedisDtoList);
	}

	@Override
	public BatchActivePawCardResponse getFinalResponse(Long cloverId) {
		List<DeckRedisDto> deckRedisDtoList = deckRedisService.getDecks(cloverId);
		PawCardEffectDto pawCardEffectDto = pawCardEffectRedisService.getEffect(cloverId);

		if (deckRedisDtoList == null || deckRedisDtoList.isEmpty()) {
			throw new DeckException(DECK_CACHE_MISS);
		}

		if (pawCardEffectDto == null) {
			throw new DeckException(PAW_CARD_EFFECT_CACHE_MISS);
		}
		return BatchActivePawCardResponse.from(deckRedisDtoList, pawCardEffectDto);
	}

}
