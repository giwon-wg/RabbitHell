package com.example.rabbithell.domain.deck.service;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.deck.dto.request.BatchActivePawCardRequest;
import com.example.rabbithell.domain.deck.dto.request.DeckCond;
import com.example.rabbithell.domain.deck.dto.response.BatchActivePawCardResponse;
import com.example.rabbithell.domain.deck.dto.response.DeckResponse;

public interface DeckService {
	PageResponse<DeckResponse> findAllDeckByCond(Long cloverId, int pageNumber, int size, DeckCond cond);

	DeckResponse findDeckById(Long deckId, Long cloverId);

	BatchActivePawCardResponse activePawCard(Long cloverId, BatchActivePawCardRequest request);
}
