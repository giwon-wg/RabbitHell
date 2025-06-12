package com.example.rabbithell.domain.deck.dto;

import java.time.LocalDateTime;

import com.example.rabbithell.domain.deck.entity.Deck;
import com.example.rabbithell.domain.deck.enums.PawCardSlot;
import com.example.rabbithell.domain.pawcard.enums.CardEmblem;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeckRedisDto {
	private Long deckId;
	private Long cloverId;
	private Long pawCardId;
	private Integer cardNumber;
	private CardEmblem cardEmblem;
	private Integer ratioPercent;
	private String description;
	private PawCardSlot pawCardSlot;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public DeckRedisDto(
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
		this.deckId = deckId;
		this.cloverId = cloverId;
		this.pawCardId = pawCardId;
		this.cardNumber = cardNumber;
		this.cardEmblem = cardEmblem;
		this.ratioPercent = ratioPercent;
		this.description = description;
		this.pawCardSlot = pawCardSlot;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public static DeckRedisDto fromEntity(Deck deck) {
		return new DeckRedisDto(
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
