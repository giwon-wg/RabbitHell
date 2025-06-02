package com.example.rabbithell.domain.inventory.dto.response;

import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.enums.Slot;

public record UnequipResponse(
	Long inventoryItemId,
	Long itemId,
	Slot slot,
	Integer durability
) {
	public static UnequipResponse fromEntity(InventoryItem inventoryItem) {
		return new UnequipResponse(
			inventoryItem.getId(),
			inventoryItem.getItem().getId(),
			inventoryItem.getSlot(),
			inventoryItem.getDurability()
		);
	}
}
