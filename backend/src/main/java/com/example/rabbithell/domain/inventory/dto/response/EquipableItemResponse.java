package com.example.rabbithell.domain.inventory.dto.response;

import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.enums.Slot;
import com.example.rabbithell.domain.item.enums.ItemType;
import com.example.rabbithell.domain.item.enums.Rarity;

public record EquipableItemResponse(
	Long inventoryItemId,
	Long inventoryId,
	Long itemId,
	String itemName,
	String description,
	ItemType itemType,
	Rarity rarity,
	Long price,
	Long power,
	Long weight,
	Integer maxDurability,
	Integer durability,
	Slot slot
) {
	public static EquipableItemResponse fromEntity(InventoryItem inventoryItem) {
		return new EquipableItemResponse(
			inventoryItem.getId(),
			inventoryItem.getInventory().getId(),
			inventoryItem.getItem().getId(),
			inventoryItem.getItem().getName(),
			inventoryItem.getItem().getDescription(),
			inventoryItem.getItem().getItemType(),
			inventoryItem.getItem().getRarity(),
			inventoryItem.getItem().getPrice(),
			inventoryItem.getPower(),
			inventoryItem.getWeight(),
			inventoryItem.getItem().getMaxDurability(),
			inventoryItem.getDurability(),
			inventoryItem.getSlot()
		);
	}
}
