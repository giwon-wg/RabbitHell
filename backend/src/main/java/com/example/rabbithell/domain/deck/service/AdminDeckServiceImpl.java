package com.example.rabbithell.domain.deck.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.deck.dto.request.CreateDeckRequest;
import com.example.rabbithell.domain.deck.dto.response.DeckResponse;
import com.example.rabbithell.domain.deck.entity.Deck;
import com.example.rabbithell.domain.deck.repository.DeckRepository;
import com.example.rabbithell.domain.pawcard.entity.PawCard;
import com.example.rabbithell.domain.pawcard.repository.PawCardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminDeckServiceImpl implements AdminDeckService {

	private final DeckRepository deckRepository;
	private final PawCardRepository pawCardRepository;
	private final CloverRepository cloverRepository;

	@Transactional
	@Override
	public DeckResponse createDeck(Long cloverId, CreateDeckRequest request) {
		Clover clover = cloverRepository.findByIdOrElseThrow(cloverId);
		PawCard pawCard = pawCardRepository.findByIdOrElseThrow(request.pawCardId());
		Deck deck = Deck.builder()
			.clover(clover)
			.pawCard(pawCard)
			.build();
		deckRepository.save(deck);
		return DeckResponse.fromEntity(deck);
	}
}
