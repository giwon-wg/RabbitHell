package com.example.rabbithell.domain.village.dto.request;

import jakarta.validation.constraints.NotNull;

public record MoneyRequest(
	@NotNull int money
) {
}
