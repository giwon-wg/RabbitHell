package com.example.rabbithell.domain.deck.service;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.deck.dto.request.DeckCond;
import com.example.rabbithell.domain.deck.dto.response.DeckResponse;
import com.example.rabbithell.domain.deck.entity.Deck;
import com.example.rabbithell.domain.deck.repository.DeckRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeckServiceImpl implements DeckService {

	private final DeckRepository deckRepository;

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

	@Override
	public DeckResponse findDeckById(Long deckId, Long cloverId) {
		Deck deck = deckRepository.findByIdAndCloverIdOrElseThrow(deckId, cloverId);
		return DeckResponse.fromEntity(deck);
	}
}
