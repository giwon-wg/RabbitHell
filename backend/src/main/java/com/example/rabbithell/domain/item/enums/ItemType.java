package com.example.rabbithell.domain.item.enums;

import java.util.Arrays;
import java.util.List;

import com.example.rabbithell.domain.inventory.enums.Slot;

import lombok.Getter;

@Getter
public enum ItemType {
	// equipable
	SWORD(true, false),
	BOW(true, false),
	DAGGER(true, false),
	WAND(true, false),
	ARMOR(true, false),
	ACCESSORY(true, false),

	// consumable
	HP(false, true),
	MP(false, true),

	// et cetera
	ETC(false, false);

	private final boolean equipable;
	private final boolean consumable;

	ItemType(boolean equipable, boolean consumable) {
		this.equipable = equipable;
		this.consumable = consumable;
	}

	public static List<ItemType> getEquipableTypes() {
		return Arrays.stream(values())
			.filter(ItemType::isEquipable)
			.toList();
	}

	public static List<ItemType> getItemTypesBySlot(Slot slot) {
		if (slot == null) {
			return null;
		}

		return switch (slot) {
			case HEAD -> List.of(ACCESSORY);
			case BODY -> List.of(ARMOR);
			case HAND -> List.of(SWORD, BOW, DAGGER, WAND);
			default -> null;
		};
	}
}
