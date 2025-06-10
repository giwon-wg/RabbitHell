package com.example.rabbithell.common.effect.applier;

import java.math.BigDecimal;

import com.example.rabbithell.common.effect.enums.StatType;

public interface StatApplier {
	StatType supports();

	void apply(BigDecimal ratio, Object target);
}
