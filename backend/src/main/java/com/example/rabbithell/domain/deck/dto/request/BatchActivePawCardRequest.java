package com.example.rabbithell.domain.deck.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record BatchActivePawCardRequest(
	@NotEmpty(message = "PawCardSlot 활성화 요청은 최소 1개 이상이어야 합니다.")
	List<@Valid ActivePawCardRequest> activePawCardRequestList
) {
}
