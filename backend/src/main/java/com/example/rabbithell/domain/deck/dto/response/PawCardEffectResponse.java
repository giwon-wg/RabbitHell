package com.example.rabbithell.domain.deck.dto.response;

import java.util.List;

import com.example.rabbithell.domain.deck.entity.PawCardEffect;
import com.example.rabbithell.domain.deck.enums.CardRanking;

public record PawCardEffectResponse(
	Long pawCardEffectId,
	Long cloverId,
	CardRanking cardRanking,
	List<EffectDetailResponse> effectDetailResponseList
) {
	public static PawCardEffectResponse from(
		PawCardEffect pawCardEffect,
		List<EffectDetailResponse> effectDetailResponseList
	) {
		return new PawCardEffectResponse(
			pawCardEffect.getId(),
			pawCardEffect.getClover().getId(),
			pawCardEffect.getCardRanking(),
			effectDetailResponseList
		);
	}
}
