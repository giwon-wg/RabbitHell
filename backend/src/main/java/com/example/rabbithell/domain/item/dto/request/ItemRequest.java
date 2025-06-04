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

	@Schema(description = "설명", example = "장미 문양이 새겨진 날카로운 칼")
	String description,

	@Schema(description = "타입", example = "SWORD")
	ItemType itemType,

	@Schema(description = "희귀도", example = "LEGENDARY")
	Rarity rarity,

	@Schema(description = "가격", example = "150000")
	Long price,

	@Schema(description = "위력", example = "900")
	Long power,

	@Schema(description = "최대 위력", example = "1000")
	Long maxPower,

	@Schema(description = "최소 위력", example = "800")
	Long minPower,

	@Schema(description = "무게", example = "15")
	Long weight,

	@Schema(description = "내구도", example = "1000")
	Integer durability
) {
}
