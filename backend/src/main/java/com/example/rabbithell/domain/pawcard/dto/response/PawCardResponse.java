package com.example.rabbithell.domain.pawcard.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.rabbithell.common.effect.enums.DomainType;
import com.example.rabbithell.common.effect.enums.StatCategory;
import com.example.rabbithell.common.effect.enums.StatType;
import com.example.rabbithell.domain.pawcard.entity.PawCard;
import com.example.rabbithell.domain.pawcard.enums.CardEmblem;

public record PawCardResponse(
	Long stigmaId,
	BigDecimal ratio,
	String description,
	Integer cardNumber,
	CardEmblem cardEmblem,
	StatType statType,
	StatCategory statCategory,
	DomainType domainType,
	Boolean isDeleted,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt) {

	public static PawCardResponse fromEntity(PawCard pawCard) {
		return new PawCardResponse(
			pawCard.getId(),
			pawCard.getRatio(),
			pawCard.getDescription(),
			pawCard.getCardNumber(),
			pawCard.getCardEmblem(),
			pawCard.getStatType(),
			pawCard.getStatCategory(),
			pawCard.getDomainType(),
			pawCard.getIsDeleted(),
			pawCard.getCreatedAt(),
			pawCard.getModifiedAt());
	}
}
