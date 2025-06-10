package com.example.rabbithell.domain.deck.dto.response;

import java.util.List;

import com.example.rabbithell.domain.deck.entity.PawCardEffect;

public record BatchActivePawCardResponse(
	List<DeckResponse> responseList,
	PawCardEffectResponse pawCardEffectResponse
) {

	public static BatchActivePawCardResponse from(
		List<DeckResponse> responseList,
		PawCardEffectResponse pawCardEffectResponse
	) {
		return new BatchActivePawCardResponse(responseList, pawCardEffectResponse);
	}
}
