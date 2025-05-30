package com.example.rabbithell.domain.stigmasocket.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateStigmaSocketRequest(
	@NotNull Long characterId,
	@NotNull Long stigmaId
) {
}
