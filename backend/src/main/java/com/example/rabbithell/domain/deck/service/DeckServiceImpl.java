package com.example.rabbithell.domain.deck.service;

import static com.example.rabbithell.domain.deck.exception.code.DeckExceptionCode.*;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

		// 요청을 slot별로 구분 (deckId == null일 수도 있음)
		Map<PawCardSlot, Long> slotToDeckIdMap = new LinkedHashMap<>();
		for (ActivePawCardRequest r : requestList) {
			if (r.pawCardSlot() != null) {
				slotToDeckIdMap.put(r.pawCardSlot(), r.deckId());
			}
		}

		List<Deck> allDecks = deckRepository.findAllByCloverIdWithLock(cloverId);

		// 1. 해제 대상 처리
		List<Deck> decksToRelease = allDecks.stream()
			.filter(deck -> {
				PawCardSlot currentSlot = deck.getPawCardSlot();
				if (currentSlot == null) {
					return false;
				}

				if (!slotToDeckIdMap.containsKey(currentSlot)) {
					return false; // 명시적으로 해제 요청 없음 → 해제
				}

				Long requestedDeckId = slotToDeckIdMap.get(currentSlot);
				return !Objects.equals(deck.getId(), requestedDeckId); // 다른 덱이거나 null이면 해제
			})
			.toList();

		for (Deck deck : decksToRelease) {
			deck.equipDeck(null);
		}

		deckRepository.flush(); // 해제 먼저 반영

		// 2. 장착 대상 처리
		List<Deck> decksToEquip = allDecks.stream()
			.filter(deck -> {
				PawCardSlot requestedSlot = slotToDeckIdMap.entrySet().stream()
					.filter(e -> Objects.equals(e.getValue(), deck.getId()))
					.map(Map.Entry::getKey)
					.findFirst()
					.orElse(null);
				return requestedSlot != null && !requestedSlot.equals(deck.getPawCardSlot());
			})
			.toList();

		for (Deck deck : decksToEquip) {
			PawCardSlot slot = slotToDeckIdMap.entrySet().stream()
				.filter(e -> Objects.equals(e.getValue(), deck.getId()))
				.map(Map.Entry::getKey)
				.findFirst()
				.orElse(null);
			deck.equipDeck(slot);
		}

		// 3. Redis 캐싱용 최종 장착 상태 조회
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
