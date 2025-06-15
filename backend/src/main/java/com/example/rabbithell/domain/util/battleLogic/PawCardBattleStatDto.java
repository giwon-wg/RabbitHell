package com.example.rabbithell.domain.util.battlelogic;

import lombok.Getter;

@Getter
public class PawCardBattleStatDto {

	private Integer attackBonus = 0;
	private Integer defenseBonus = 0;
	private Integer speedBonus = 0;
	private Integer attackPercent = 0;

	public static PawCardBattleStatDto create() {
		return new PawCardBattleStatDto();
	}

	public void applyAttackBonus(Integer value) {
		if (value != null) {
			this.attackBonus += value;
		}
	}

	public void applyDefenseBonus(Integer value) {
		if (value != null) {
			this.defenseBonus += value;
		}
	}

	public void applySpeedBonus(Integer value) {
		if (value != null) {
			this.speedBonus += value;
		}
	}
}
