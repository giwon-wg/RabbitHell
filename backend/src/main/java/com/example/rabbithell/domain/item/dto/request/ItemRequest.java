package com.example.rabbithell.domain.item.dto.request;

import com.example.rabbithell.domain.item.enums.ItemType;
import com.example.rabbithell.domain.item.enums.Rarity;
import com.example.rabbithell.domain.shop.entity.Shop;

import io.swagger.v3.oas.annotations.media.Schema;

public record ItemRequest(
	@Schema(description = "상점 ID", example = "1")
	Long shopId,

	@Schema(description = "아이템명", example = "장미칼")
	String name,

	@Schema(description = "타입", example = "SWORD")
	ItemType itemType,

	@Schema(description = "희귀도", example = "1")
	Rarity rarity,
	Long price,
	Long attack,
	Long weight,
	Integer durability
) {
}
