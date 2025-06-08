package com.example.rabbithell.domain.battle.type;

import lombok.Getter;

@Getter
public enum BattleFieldType {
	PLAIN(false, 1,1,0),
	MOUNTAIN(false, 2,2,0),
	CAVE(false, 3,3,0),
	RIFT(false, 4,4,0),

	// 기본 필드 4개 들, 산, 동굴, 협곡


	// 특수 필드 (골드) 황금의 들, 마법의 계곡, 수정 동굴, 용의 둥지
	GOLDEN_FIELD(true, 10,1,0.1),
	MAGIC_VALLEY(true, 20,2,0.1),
	CRYSTAL_CAVE(true, 30,3,0.1),
	DRAGON_NEST(true, 40,4,0.1),

	// 특수 필드 (숙련도) 시험의 장, 현자의 숲, 정령의 신전, 공허의 투기장)
	TRIAL_FIELD(true, 100,1,0.1),
	SAGE_FOREST(true, 200,2,0.1),
	SPIRIT_TEMPLE(true, 300,3,0.1),
	VOID_ARENA(true, 400,4,0.1),

	// 특수 필드 (아이템) 전리품의 초원, 유물의 숲, 마법의 보물창고, 잊혀진 금고
	LOOT_MEADOW(true, 2,1,0.01),
	RELIC_GROVE(true, 4,2,0.01),
	ARCANE_HOARD(true, 6,3,0.01),
	FORGOTTEN_VAULT(true,8,4,0.01),

	// 특수 필드 (연결형) 희미한 균열, 황혼의 균열, 에테르 균열, 공허의 균열, 꿈의 균열, 균열의 중심
	DIM_CRACK(true, 5,1,0.02),
	TWILIGHT_CRACK(true, 50,1,0.8),
	ETHER_CRACK(true, 250,1,0.6),
	NEXUS_CRACK(true, 1000,2,0.4),
	DREAM_CRACK(true, 5000,2,0.2),
	CENTER_CRACK(true, 50000,2,0.01)


	;
	private final boolean isRare;
	private final int skillPoints;
	private final int level;
	private final double appearanceRate;
	BattleFieldType(boolean isRare, int skillPoints, int level, double appearanceRate) {
		this.isRare = isRare;
		this.skillPoints = skillPoints;
		this.level = level;
		this.appearanceRate = appearanceRate;
	}

}
