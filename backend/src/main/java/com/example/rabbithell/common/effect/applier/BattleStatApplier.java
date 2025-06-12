package com.example.rabbithell.common.effect.applier;

import com.example.rabbithell.common.effect.enums.StatType;
import com.example.rabbithell.domain.util.battleLogic.BattleStatDto;

public interface BattleStatApplier {
	StatType supports();

	void apply(Integer ratioPercent, BattleStatDto battleStatDto);
}
