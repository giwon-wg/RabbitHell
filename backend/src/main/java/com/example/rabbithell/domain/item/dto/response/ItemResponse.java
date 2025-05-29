package com.example.rabbithell.domain.item.dto.response;

import com.example.rabbithell.domain.item.entity.Item;
import com.example.rabbithell.domain.item.enums.Rarity;
import com.example.rabbithell.domain.shop.entity.Shop;

public record ItemResponse(
    Long itemId,
    Shop shop,
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
            item.getShop(),
            item.getName(),
            item.getRarity(),
            item.getPrice(),
            item.getAttack(),
            item.getWeight(),
            item.getDurability()
        );
    }
}
