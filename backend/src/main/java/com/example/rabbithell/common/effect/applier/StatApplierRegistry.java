package com.example.rabbithell.common.effect.applier;

import static com.example.rabbithell.common.effect.applier.exception.code.CardEffectErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.rabbithell.common.effect.applier.exception.CardEffectException;
import com.example.rabbithell.common.effect.enums.StatType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StatApplierRegistry {

	private final List<StatApplier> appliers;

	public StatApplier get(StatType statType) {
		return appliers.stream()
			.filter(a -> a.supports() == statType)
			.findFirst()
			.orElseThrow(() -> new CardEffectException(STAT_NOT_FOUND));
	}
}
