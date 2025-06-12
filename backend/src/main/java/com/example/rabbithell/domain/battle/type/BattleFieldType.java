package com.example.rabbithell.domain.battle.type;

import com.example.rabbithell.domain.monster.enums.Rating;

import lombok.Getter;

@Getter
public enum BattleFieldType {
	PLAIN("들", false, 1, 1, 0),
	MOUNTAIN("산", false, 2, 2, 0),
	CAVE("동굴", false, 3, 3, 0),
	RIFT("협곡", false, 4, 4, 0),

	// 기본 필드 4개 들, 산, 동굴, 협곡

	// 특수 필드 (골드) 황금의 들, 마법의 계곡, 수정 동굴, 용의 둥지
	GOLDEN_FIELD("황금의 들", true, 10, 1, 0.1),
	MAGIC_VALLEY("마법의 계곡", true, 20, 2, 0.1),
	CRYSTAL_CAVE("수정 동굴", true, 30, 3, 0.1),
	DRAGON_NEST("용의 둥지", true, 40, 4, 0.1),

	// 특수 필드 (숙련도) 시험의 장, 현자의 숲, 정령의 신전, 공허의 투기장)
	TRIAL_FIELD("시험의 장", true, 100, 1, 0.1),
	SAGE_FOREST("현자의 숲", true, 200, 2, 0.1),
	SPIRIT_TEMPLE("정령의 신전", true, 300, 3, 0.1),
	VOID_ARENA("공허의 투기장", true, 400, 4, 0.1),

	// 특수 필드 (아이템) 전리품의 초원, 유물의 숲, 마법의 보물창고, 잊혀진 금고
	LOOT_MEADOW("전리품의 초원", true, 2, 1, 0.01),
	RELIC_GROVE("유물의 숲", true, 4, 2, 0.01),
	ARCANE_HOARD("마법의 보물창고", true, 6, 3, 0.01),
	FORGOTTEN_VAULT("잊혀진 금고", true, 8, 4, 0.01),

	// 특수 필드 (연결형) 희미한 균열, 황혼의 균열, 에테르 균열, 공허의 균열, 꿈의 균열, 균열의 중심
	DIM_CRACK("희미한 균열", true, 5, 100, 0.8),
	TWILIGHT_CRACK("황혼의 균열", true, 50, 101, 0.8),
	ETHER_CRACK("에테르 균열", true, 250, 102, 0.6),
	NEXUS_CRACK("공허의 균열", true, 1000, 103, 0.4),
	DREAM_CRACK("꿈의 균열", true, 5000, 104, 0.2),
	CENTER_CRACK("균열의 중심", true, 50000, 105, 0.01);

	private final String name;
	private final boolean isRare;
	private final int skillPoints;
	private final int level;
	private final double appearanceRate;

	BattleFieldType(String name, boolean isRare, int skillPoints, int level, double appearanceRate) {
		this.name = name;
		this.isRare = isRare;
		this.skillPoints = skillPoints;
		this.level = level;
		this.appearanceRate = appearanceRate;
	}

	public int calculateSkillPointBy(Rating rating) {
		return this.skillPoints * rating.getSkillPointMultiplier();

	}

}
