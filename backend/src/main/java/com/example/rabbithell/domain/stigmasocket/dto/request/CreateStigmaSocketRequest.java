package com.example.rabbithell.domain.stigmasocket.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CreateStigmaSocketRequest(
	@Schema(description = "캐릭터id", example = "1")
	@NotNull Long characterId,
	@Schema(description = "스티그마id", example = "1")
	@NotNull Long stigmaId
) {
}
