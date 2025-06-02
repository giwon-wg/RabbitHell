package com.example.rabbithell.domain.inventory.dto.response;

import com.example.rabbithell.domain.inventory.entity.InventoryItem;

public record UseResponse(
	Long inventoryItemId,
	Integer durability
) {
	public static UseResponse fromEntity(InventoryItem inventoryItem) {
		return new UseResponse(
			inventoryItem.getId(),
			inventoryItem.getDurability()
		);
	}
}
