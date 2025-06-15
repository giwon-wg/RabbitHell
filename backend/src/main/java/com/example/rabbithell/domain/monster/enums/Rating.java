package com.example.rabbithell.domain.monster.enums;

import lombok.Getter;

public enum Rating {
	COMMON(1), RARE(10), ELITE(20), MINI_BOSS(30), BOSS(100), SPECIAL(7);

	@Getter
	private final int skillPointMultiplier;

	Rating(int skillPointMultiplier) {
		this.skillPointMultiplier = skillPointMultiplier;
	}
}
