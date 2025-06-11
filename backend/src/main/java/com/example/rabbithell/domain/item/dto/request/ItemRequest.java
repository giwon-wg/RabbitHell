package com.example.rabbithell.domain.item.dto.request;

import com.example.rabbithell.domain.item.enums.ItemType;
import com.example.rabbithell.domain.item.enums.Rarity;

public record ItemRequest(
	Long shopId,
	Long effectId,
	String name,
	String description,
	ItemType itemType,
	Rarity rarity,
	Long price,
	Long power,
	Long maxPower,
	Long minPower,
	Long maxWeight,
	Long minWeight,
	Integer maxDurability
) {
}
