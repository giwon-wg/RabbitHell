package com.example.rabbithell.domain.deck.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.rabbithell.domain.deck.dto.request.DeckCond;
import com.example.rabbithell.domain.deck.entity.Deck;

public interface DeckQueryRepository {
	List<Deck> findAllByCondition(Long cloverId, DeckCond cond, Pageable pageable);

	long countByCondition(Long cloverId, DeckCond cond);

	Deck findByIdAndCloverIdOrElseThrow(Long deckId, Long cloverId);
}
