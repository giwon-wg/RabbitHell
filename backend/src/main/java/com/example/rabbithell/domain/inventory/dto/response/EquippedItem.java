package com.example.rabbithell.domain.inventory.dto.response;

import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.enums.Slot;

public record EquippedItem(
	Long inventoryItemId,
	Long itemId,
	String name,
	Long characterId,
	String characterName,
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
			inventoryItem.getSlot(),
			inventoryItem.getDurability()
		);
	}
}
