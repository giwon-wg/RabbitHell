package com.example.rabbithell.domain.battle.type;

import lombok.Getter;

@Getter
public enum BattleFieldType {
	FIELD(false, 1),
	MOUNTAIN(false, 2),
	CAVE(false, 3),
	RIFT(false, 4),

	// 기본 필드 4개 들, 산, 동굴, 협곡


	// 특수 필드 (골드) 황금의 들, 마법의 계곡, 수정 동굴, 용의 둥지
	GOLDEN_FIELD(true, 10),
	MAGIC_VALLEY(true, 20),
	CRYSTAL_CAVE(true, 30),
	DRAGON_NEST(true, 40),

	// 특수 필드 (숙련도) 시험의 장, 현자의 숲, 정령의 신전, 공허의 투기장)
	TRIAL_FIELD(true, 100),
	SAGE_FOREST(true, 200),
	SPIRIT_TEMPLE(true, 300),
	VOID_ARENA(true, 400),

	// 특수 필드 (아이템) 전리품의 초원, 유물의 숲, 마법의 보물창고, 잊혀진 금고
	LOOT_MEADOW(true, 2),
	RELIC_GROVE(true, 4),
	ARCANE_HOARD(true, 6),
	FORGOTTEN_VAULT(true,8),

	// 특수 필드 (연결형) 희미한 균열, 황혼의 균열, 에테르 균열, 공허의 균열, 꿈의 균열, 균열의 중심
	DIM_CRACK(true, 5),
	TWILIGHT_CRACK(true, 50),
	ETHER_CRACK(true, 250),
	NEXUS_CRACK(true, 1000),
	DREAM_CRACK(true, 5000),
	CENTER_CRACK(true, 50000)

	//




	;
	private final boolean isRare;
	private final int skillPoints;

	BattleFieldType(boolean isRare, int skillPoints) {
		this.isRare = isRare;
		this.skillPoints = skillPoints;
	}

}
