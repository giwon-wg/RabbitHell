package com.example.rabbithell.domain.deck.service;

import com.example.rabbithell.domain.deck.dto.request.CreateDeckRequest;
import com.example.rabbithell.domain.deck.dto.response.DeckResponse;

public interface AdminDeckService {
	DeckResponse createDeck(Long cloverId, CreateDeckRequest request);
}
