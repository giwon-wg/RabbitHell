package com.example.rabbithell.domain.deck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.deck.entity.Deck;

public interface DeckRepository extends JpaRepository<Deck, Long> {
}
