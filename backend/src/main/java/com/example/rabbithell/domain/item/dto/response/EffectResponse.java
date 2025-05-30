package com.example.rabbithell.domain.item.dto.response;

import com.example.rabbithell.domain.item.entity.Effect;
import com.example.rabbithell.domain.item.enums.EffectType;

public record EffectResponse(
	Long effectId,
	EffectType effectType,
	Long power
) {
	public static EffectResponse fromEntity(Effect effect) {
		return new EffectResponse(
			effect.getId(),
			effect.getEffectType(),
			effect.getPower()
		);
	}
}
