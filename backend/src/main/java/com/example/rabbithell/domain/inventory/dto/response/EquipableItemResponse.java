package com.example.rabbithell.domain.inventory.dto.response;

import com.example.rabbithell.domain.inventory.entity.InventoryItem;

public record EquipableItemResponse(
	Long itemId,
	String name
) {
	public static EquipableItemResponse fromEntity(InventoryItem inventoryItem) {
		return new EquipableItemResponse(
			inventoryItem.getItem().getId(),
			inventoryItem.getItem().getName()
		);
	}
}
