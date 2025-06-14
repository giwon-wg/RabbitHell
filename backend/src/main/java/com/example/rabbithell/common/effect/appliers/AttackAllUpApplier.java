package com.example.rabbithell.common.effect.appliers;

import org.springframework.stereotype.Component;

import com.example.rabbithell.common.effect.applier.BattleStatApplier;
import com.example.rabbithell.common.effect.enums.StatType;
import com.example.rabbithell.domain.util.battlelogic.PawCardBattleStatDto;

@Component
public class AttackAllUpApplier implements BattleStatApplier {

	@Override
	public StatType supports() {
		return StatType.ATTACK_All_UP;
	}

	@Override
	public void apply(Integer ratioPercent, PawCardBattleStatDto pawCardBattleStatDto) {

		pawCardBattleStatDto.applyAttackBonus(ratioPercent);
	}
}
