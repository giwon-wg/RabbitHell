package com.example.rabbithell.domain.shop.dto.response;

import com.example.rabbithell.domain.item.entity.Item;
import com.example.rabbithell.domain.item.enums.Rarity;

public record ShopItemResponse(
	Long shopId,
	String shopName,
	Long itemId,
	String itemName,
	String description,
	Rarity rarity,
	Long price,
	Long maxPower,
	Long minPower,
	Long maxWeight,
	Long minWeight,
	Integer maxDurability
) {
	public static ShopItemResponse fromEntity(Item item) {
		return new ShopItemResponse(
			item.getShop().getId(),
			item.getShop().getName(),
			item.getId(),
			item.getName(),
			item.getDescription(),
			item.getRarity(),
			item.getPrice(),
			item.getMaxPower(),
			item.getMinPower(),
			item.getMaxWeight(),
			item.getMinWeight(),
			item.getMaxDurability()
		);
	}
}
