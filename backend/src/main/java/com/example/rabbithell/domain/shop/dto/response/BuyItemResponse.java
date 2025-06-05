package com.example.rabbithell.domain.shop.dto.response;

import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.item.entity.Item;

public record BuyItemResponse(
	Long inventoryId,
	Long itemId,
	String itemName,
	Integer quantity, // 구매한 개수
	Integer remainingSlots, // 남은 인벤토리 용량
	Long cash // 남은 현금
) {
	public static BuyItemResponse of(Inventory inventory, Item item, Integer quantity, Integer remainingSlots,
		Long cash) {
		return new BuyItemResponse(
			inventory.getId(),
			item.getId(),
			item.getName(),
			quantity,
			remainingSlots,
			cash
		);
	}
}
