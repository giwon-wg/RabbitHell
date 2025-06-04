package com.example.rabbithell.domain.inventory.dto.response;

import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.enums.Slot;

public record InventoryItemResponse(
	Long inventoryId,
	Long itemId,
	String itemName,
	Integer durability,
	Slot slot
) {
	public static InventoryItemResponse fromEntity(InventoryItem inventoryItem) {
		return new InventoryItemResponse(
			inventoryItem.getInventory().getId(),
			inventoryItem.getItem().getId(),
			inventoryItem.getItem().getName(),
			inventoryItem.getDurability(),
			inventoryItem.getSlot()
		);
	}
}
