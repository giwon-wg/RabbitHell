package com.example.rabbithell.domain.shop.dto.response;

import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.item.entity.Item;

public record SellItemResponse(
	Long inventoryId,
	Long itemId,
	String itemName,
	Long price,
	Integer remainingSlots,
	Long cash
) {
	public static SellItemResponse of(Inventory inventory, Item item, Integer remainingSlots, Long cash) {
		return new SellItemResponse(
			inventory.getId(),
			item.getId(),
			item.getName(),
			item.getPrice(),
			remainingSlots,
			cash
		);
	}
}
