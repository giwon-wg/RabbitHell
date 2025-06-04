package com.example.rabbithell.domain.deck.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.deck.entity.Deck;
import com.example.rabbithell.domain.deck.enums.PawCardSlot;
import com.example.rabbithell.domain.pawcard.entity.PawCard;
import com.example.rabbithell.domain.pawcard.enums.CardEmblem;

public record DeckResponse(
	Long deckId,
	Long cloverId,
	Long pawCardId,
	Integer cardNumber,
	CardEmblem cardEmblem,
	BigDecimal ratio,
	String description,
	PawCardSlot slot,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {
	public static DeckResponse fromEntity(
		Deck deck,
		Clover clover,
		PawCard pawCard) {
		return new DeckResponse(
			deck.getId(),
			clover.getId(),
			pawCard.getId(),
			pawCard.getCardNumber(),
			pawCard.getCardEmblem(),
			pawCard.getRatio(),
			pawCard.getDescription(),
			deck.getPawCardSlot(),
			deck.getCreatedAt(),
			deck.getModifiedAt()
		);
	}
}
