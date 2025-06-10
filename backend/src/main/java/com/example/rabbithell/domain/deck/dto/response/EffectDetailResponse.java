package com.example.rabbithell.domain.deck.dto.response;

import com.example.rabbithell.common.effect.enums.StatType;
import com.example.rabbithell.domain.deck.entity.EffectDetail;
import com.example.rabbithell.domain.deck.enums.EffectDetailSlot;

public record EffectDetailResponse(
	Long effectDetailId,
	EffectDetailSlot effectDetailSlot,
	StatType statType,
	Integer finalEffectValue
) {
	public static EffectDetailResponse fromEntity(
		EffectDetail effectDetail
	) {
		return new EffectDetailResponse(
			effectDetail.getId(),
			effectDetail.getEffectDetailSlot(),
			effectDetail.getStatType(),
			effectDetail.getFinalEffectValue()
		);
	}
}
