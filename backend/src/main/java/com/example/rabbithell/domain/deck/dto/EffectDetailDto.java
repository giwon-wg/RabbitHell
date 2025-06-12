package com.example.rabbithell.domain.deck.dto;

import com.example.rabbithell.common.effect.enums.StatType;
import com.example.rabbithell.domain.deck.entity.EffectDetail;
import com.example.rabbithell.domain.deck.enums.EffectDetailSlot;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EffectDetailDto {
	Long effectDetailId;
	EffectDetailSlot effectDetailSlot;
	StatType statType;
	Integer finalEffectValue;

	EffectDetailDto(
		Long effectDetailId,
		EffectDetailSlot effectDetailSlot,
		StatType statType,
		Integer finalEffectValue
	) {
		this.effectDetailId = effectDetailId;
		this.effectDetailSlot = effectDetailSlot;
		this.statType = statType;
		this.finalEffectValue = finalEffectValue;
	}

	public static EffectDetailDto fromEntity(
		EffectDetail effectDetail
	) {
		return new EffectDetailDto(
			effectDetail.getId(),
			effectDetail.getEffectDetailSlot(),
			effectDetail.getStatType(),
			effectDetail.getFinalEffectValue()
		);
	}
}
