package com.example.rabbithell.domain.deck.dto;

import java.util.List;

import com.example.rabbithell.domain.deck.entity.PawCardEffect;
import com.example.rabbithell.domain.deck.enums.CardRanking;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PawCardEffectDto {
	private Long pawCardEffectId;
	private Long cloverId;
	private CardRanking cardRanking;
	private List<EffectDetailDto> effectDetailDtoList;

	PawCardEffectDto (
		Long pawCardEffectId,
		Long cloverId,
		CardRanking cardRanking,
		List<EffectDetailDto> effectDetailDtoList
	) {
		this.pawCardEffectId = pawCardEffectId;
		this.cloverId = cloverId;
		this.cardRanking = cardRanking;
		this.effectDetailDtoList = effectDetailDtoList;
	}
	public static PawCardEffectDto from(
		PawCardEffect pawCardEffect,
		List<EffectDetailDto> effectDetailDtoList
	) {
		return new PawCardEffectDto(
			pawCardEffect.getId(),
			pawCardEffect.getClover().getId(),
			pawCardEffect.getCardRanking(),
			effectDetailDtoList
		);
	}
}
