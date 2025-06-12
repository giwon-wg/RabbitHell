package com.example.rabbithell.common.effect.applier;

import static com.example.rabbithell.common.effect.applier.exception.code.CardEffectErrorCode.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.rabbithell.common.effect.applier.exception.CardEffectException;
import com.example.rabbithell.common.effect.enums.StatType;

import jakarta.annotation.PostConstruct;

@Component
public class BattleStatApplierRegistry {

	private final List<BattleStatApplier> appliers;
	private Map<StatType, BattleStatApplier> applierMap;

	public BattleStatApplierRegistry(List<BattleStatApplier> appliers) {
		this.appliers = appliers;
	}

	@PostConstruct
	public void init() {
		this.applierMap = appliers.stream()
			.collect(Collectors.toMap(BattleStatApplier::supports, a -> a));
	}

	public BattleStatApplier get(StatType statType) {
		BattleStatApplier applier = applierMap.get(statType);
		if (applier == null) {
			throw new CardEffectException(STAT_NOT_FOUND);
		}
		return applier;
	}
}
