package com.example.rabbithell.domain.inventory.enums;

import java.util.List;

import com.example.rabbithell.domain.item.enums.ItemType;

public enum Slot {
	HEAD, BODY, HAND;

	public static Slot getSlotByItemType(ItemType itemType) {
		if (itemType == null) {
			return null;
		}

		for (Slot slot : Slot.values()) {
			List<ItemType> types = ItemType.getItemTypesBySlot(slot);
			if (types != null && types.contains(itemType)) {
				return slot;
			}
		}

		return null; // 해당하는 슬롯이 없을 경우
	}
}
