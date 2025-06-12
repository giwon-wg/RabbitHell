package com.example.rabbithell.common.effect.applier;

import com.example.rabbithell.common.effect.enums.StatType;
import com.example.rabbithell.domain.util.battleLogic.PawCardBattleStatDto;

public interface BattleStatApplier {
	StatType supports();

	void apply(Integer ratioPercent, PawCardBattleStatDto pawCardBattleStatDto);
}
