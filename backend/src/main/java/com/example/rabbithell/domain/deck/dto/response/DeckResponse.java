package com.example.rabbithell.domain.deck.dto.response;

import java.time.LocalDateTime;

import com.example.rabbithell.domain.deck.entity.Deck;
import com.example.rabbithell.domain.deck.enums.PawCardSlot;
import com.example.rabbithell.domain.pawcard.enums.CardEmblem;

public record DeckResponse(
	Long deckId,
	Long cloverId,
	Long pawCardId,
	Integer cardNumber,
	CardEmblem cardEmblem,
	Integer ratioPercent,
	String description,
	PawCardSlot pawCardSlot,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {
	public static DeckResponse fromEntity(Deck deck) {
		return new DeckResponse(
			deck.getId(),
			deck.getClover().getId(),
			deck.getPawCard().getId(),
			deck.getPawCard().getCardNumber(),
			deck.getPawCard().getCardEmblem(),
			deck.getPawCard().getRatioPercent(),
			deck.getPawCard().getDescription(),
			deck.getPawCardSlot(),
			deck.getCreatedAt(),
			deck.getModifiedAt()
		);
	}
}
