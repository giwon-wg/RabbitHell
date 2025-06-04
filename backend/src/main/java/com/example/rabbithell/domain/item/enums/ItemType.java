package com.example.rabbithell.domain.item.enums;

import java.util.Arrays;
import java.util.List;

public enum ItemType {
	// equipable
	SWORD, SHIELD, BOW, DAGGER,

	// consumable
	HP, MP;

	public static List<ItemType> getEquipableTypes() {
		return Arrays.stream(values())
			.filter(ItemType::isEquipable)
			.toList();
	}

	public boolean isEquipable() {
		return switch (this) {
			case SWORD, SHIELD, BOW, DAGGER -> true;
			default -> false;
		};
	}

	public boolean isConsumable() {
		return switch (this) {
			case HP, MP -> true;
			default -> false;
		};
	}
}
