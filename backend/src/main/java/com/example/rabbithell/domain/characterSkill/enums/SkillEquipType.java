package com.example.rabbithell.domain.characterSkill.enums;

public enum SkillEquipType {
	ACTIVE_SLOT_1,
	ACTIVE_SLOT_2,
	PASSIVE_SLOT,
	NONE;

	public boolean isActiveSlot() {
		return this == ACTIVE_SLOT_1 || this == ACTIVE_SLOT_2;
	}

	public boolean isPassiveSlot() {
		return this == PASSIVE_SLOT;
	}
}
