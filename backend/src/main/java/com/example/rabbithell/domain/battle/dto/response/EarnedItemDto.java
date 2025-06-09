package com.example.rabbithell.domain.battle.dto.response;

import com.example.rabbithell.domain.item.entity.Item;

public record EarnedItemDto(Long id, String name, String description) {
	public static EarnedItemDto from(Item item) {
		return new EarnedItemDto(item.getId(), item.getName(), item.getDescription());
	}
}
