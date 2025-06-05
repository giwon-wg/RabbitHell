package com.example.rabbithell.domain.item.enums;

import java.util.Arrays;
import java.util.List;

import com.example.rabbithell.domain.inventory.enums.Slot;

public enum ItemType {
	// equipable
	SWORD, SHIELD, BOW, DAGGER, CLOTHES, EARRINGS,

	// consumable
	HP, MP;

	public static List<ItemType> getEquipableTypes() {
		return Arrays.stream(values())
			.filter(ItemType::isEquipable)
			.toList();
	}

	public static List<ItemType> getItemTypeBySlot(Slot slot) {
		if (slot == null) {
			return null;
		}

		return switch (slot) {
			case HEAD -> List.of(EARRINGS);
			case BODY -> List.of(SHIELD, CLOTHES);
			case HAND -> List.of(SWORD, BOW, DAGGER);
			default -> null;
		};
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
