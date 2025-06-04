package com.example.rabbithell.domain.inventory.dto.response;

import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.enums.Slot;

public record EquippedItem(
	Long inventoryItemId,
	Long itemId,
	String itemName,
	Long characterId,
	String characterName,
	String description,
	Long power,
	Slot slot,
	Integer durability
) {
	public static EquippedItem fromEntity(InventoryItem inventoryItem) {
		return new EquippedItem(
			inventoryItem.getId(),
			inventoryItem.getItem().getId(),
			inventoryItem.getItem().getName(),
			inventoryItem.getCharacter().getId(),
			inventoryItem.getCharacter().getName(),
			inventoryItem.getItem().getDescription(),
			inventoryItem.getItem().getPower(),
			inventoryItem.getSlot(),
			inventoryItem.getDurability()
		);
	}
}
