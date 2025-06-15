package com.example.rabbithell.domain.deck.dto.request;

import com.example.rabbithell.domain.deck.enums.PawCardSlot;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ActivePawCardRequest(
	@Schema(description = "덱Id", example = "1")
	Long deckId,
	@Schema(description = "포카드 슬롯1~4", example = "PAW_CARD_SLOT1")
	@NotNull
	PawCardSlot pawCardSlot
) {
}
