package com.example.rabbithell.domain.battle.type;

import lombok.Getter;

@Getter
public enum BattleFieldType {
	PLAIN(false, 1),
	MOUNTAIN(false, 2),
	FOREST(false, 3),
	DESERT(false, 4),
	DARK_VALLEY(true, 30),
	DRAGON_NEST(true, 30),
	GOLDEN_FIELD(true, 30);

	private final boolean isRare;
	private final int skillPoints;

	BattleFieldType(boolean isRare, int skillPoints) {
		this.isRare = isRare;
		this.skillPoints = skillPoints;
	}

}
