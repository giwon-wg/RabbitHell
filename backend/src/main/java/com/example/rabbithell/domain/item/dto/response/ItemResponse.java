package com.example.rabbithell.domain.item.dto.response;

import com.example.rabbithell.domain.item.entity.Item;
import com.example.rabbithell.domain.item.enums.Rarity;

public record ItemResponse(
	Long itemId,
	String itemName,
	Long shopId,
	String shopName,
	String description,
	Rarity rarity,
	Long price,
	Long maxPower,
	Long minPower,
	Long weight,
	Integer durability
) {
	public static ItemResponse fromEntity(Item item) {
		return new ItemResponse(
			item.getId(),
			item.getName(),
			item.getShop() != null ? item.getShop().getId() : null,
			item.getShop() != null ? item.getShop().getName() : null,
			item.getDescription(),
			item.getRarity(),
			item.getPrice(),
			item.getMaxPower(),
			item.getMinPower(),
			item.getWeight(),
			item.getMaxDurability()
		);
	}
}
