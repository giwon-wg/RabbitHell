package com.example.rabbithell.common.effect.enums;

public enum StatType {
	ATTACK_UP(StatCategory.BATTLE_STAT, DomainType.GAME_CHARACTER),
	GOLD_RATE(StatCategory.REWARD_MODIFIER, DomainType.CLOVER),
	DISCOUNT(StatCategory.META_BUFF, DomainType.CLOVER),
	STRAIGHT_STAT(StatCategory.BATTLE_STAT, DomainType.CLOVER),
	FLUSH_STAT(StatCategory.REWARD_MODIFIER, DomainType.CLOVER),
	STRAIGHT_FLUSH_STAT(StatCategory.META_BUFF, DomainType.CLOVER),
	ROYAL_STRAIGHT_FLUSH_STAT(StatCategory.META_BUFF, DomainType.CLOVER);

	private final StatCategory category;
	private final DomainType domainType;

	StatType(StatCategory category, DomainType domainType) {
		this.category = category;
		this.domainType = domainType;
	}

	public StatCategory getCategory() {
		return category;
	}

	public DomainType getDomainType() {
		return domainType;
	}
}
