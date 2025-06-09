package com.example.rabbithell.domain.deck.service;

import static com.example.rabbithell.domain.deck.exception.code.DeckExceptionCode.*;

import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.deck.dto.request.ActivePawCardRequest;
import com.example.rabbithell.domain.deck.dto.request.BatchActivePawCardRequest;
import com.example.rabbithell.domain.deck.dto.request.DeckCond;
import com.example.rabbithell.domain.deck.dto.response.BatchActivePawCardResponse;
import com.example.rabbithell.domain.deck.dto.response.DeckResponse;
import com.example.rabbithell.domain.deck.dto.response.EffectDetailResponse;
import com.example.rabbithell.domain.deck.dto.response.PawCardEffectResponse;
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

	@Transactional
	@Override
	public BatchActivePawCardResponse activePawCard(Long cloverId, BatchActivePawCardRequest requests) {

		// 1. 해당 clover에서 요청된 슬롯을 이미 사용하는 덱을 조회 (락 걸기)
		List<ActivePawCardRequest> activePawCardRequests = requests.activePawCardRequestList();
		List<PawCardSlot> pawCardSlots =
			activePawCardRequests.stream()
				.map(ActivePawCardRequest::pawCardSlot)
				.toList();

		List<Deck> occupiedDecks = deckRepository.findLockedByCloverIdAndSlots(cloverId, pawCardSlots);

		// 2. 기존 슬롯 null로 해제
		for (Deck deck : occupiedDecks) {
			deck.equipDeck(null);
		}

		// 3. 요청된 deckId들을 조회
		List<Long> deckIds = activePawCardRequests.stream().map(ActivePawCardRequest::deckId).toList();
		List<Deck> targetDecks = deckRepository.lockDecksByCloverIdAndIds(cloverId, deckIds);

		// 4. 요청에 따라 슬롯 할당
		for (Deck deck : targetDecks) {
			PawCardSlot slot = activePawCardRequests.stream()
				.filter(req -> req.deckId().equals(deck.getId()))
				.findFirst()
				.orElseThrow(() -> new DeckException(INVALID_DECK_REQUEST))
				.pawCardSlot();
			deck.equipDeck(slot);
		}

		// 4. 현재 장착된 덱 정보로 효과 계산
		List<Deck> equippedDecks = deckRepository.findEquippedByCloverIdWithLock(cloverId);

		PawCardEffect effect = pawCardEffectRepository.findByCloverIdWithDetails(cloverId);

		List<EffectDetail> details = effect.getDetails();
		if (details.size() < 2)
			throw new DeckException(INVALID_EFFECT_STRUCTURE);

		// 5. 효과 계산
		deckEffectProcessor.processAllEffects(equippedDecks, effect, details);

		// 6. dto 변환
		return toBatchActivePawCardResponse(equippedDecks, effect, details);

	}

	private BatchActivePawCardResponse toBatchActivePawCardResponse(
		List<Deck> equippedDecks,
		PawCardEffect effect,
		List<EffectDetail> details
	) {
		List<DeckResponse> deckResponseList = equippedDecks.stream()
			.map(DeckResponse::fromEntity)
			.sorted(Comparator.comparing(DeckResponse::pawCardSlot, Comparator.nullsLast(Comparator.naturalOrder())))
			.toList();

		List<EffectDetailResponse> effectDetailResponseList = details.stream()
			.map(EffectDetailResponse::fromEntity)
			.toList();

		PawCardEffectResponse pawCardEffectResponse = PawCardEffectResponse.from(effect, effectDetailResponseList);

		return BatchActivePawCardResponse.from(deckResponseList, pawCardEffectResponse);
	}
}
