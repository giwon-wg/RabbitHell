package com.example.rabbithell.domain.shop.dto.response;

import com.example.rabbithell.domain.item.enums.Rarity;

public record BuyItemResponse(
	Long inventoryItemId,
	Long itemId,
	String itemName,
	String description,
	Rarity rarity,
	Long price,
	Long power,
	Long weight,
	Integer durability
) {

}
