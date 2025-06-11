package com.example.rabbithell.domain.battle.dto.response;

import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.item.entity.Effect;

public record ItemDto(
	Long id,
	String name,
	Long power,
	Long weight,
	Effect effect
) {

	public static ItemDto from(InventoryItem item) {
		return new ItemDto(
			item.getId(),
			item.getItem().getName(),
			item.getPower(),
			item.getWeight(),
			item.getItem().getEffect()
		);
	}
}
