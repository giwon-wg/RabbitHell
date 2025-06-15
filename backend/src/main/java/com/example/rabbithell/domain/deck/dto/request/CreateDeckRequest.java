package com.example.rabbithell.domain.deck.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CreateDeckRequest(
	@Schema(description = "포카드Id", example = "1")
	@NotNull Long pawCardId
) {
}
