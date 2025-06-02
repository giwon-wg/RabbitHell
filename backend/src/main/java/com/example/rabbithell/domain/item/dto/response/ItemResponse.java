package com.example.rabbithell.domain.item.dto.response;

import com.example.rabbithell.domain.item.entity.Item;
import com.example.rabbithell.domain.item.enums.Rarity;

public record ItemResponse(
	Long itemId,
	Long shopId,
	String name,
	Rarity rarity,
	Long price,
	Long attack,
	Long weight,
	Integer durability
) {
	public static ItemResponse fromEntity(Item item) {
		return new ItemResponse(
			item.getId(),
			item.getShop() != null ? item.getShop().getId() : null,
			item.getName(),
			item.getRarity(),
			item.getPrice(),
			item.getPower(),
			item.getWeight(),
			item.getDurability()
		);
	}
}
