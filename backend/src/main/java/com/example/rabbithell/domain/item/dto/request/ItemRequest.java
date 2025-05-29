package com.example.rabbithell.domain.item.dto.request;

import com.example.rabbithell.domain.item.enums.ItemType;
import com.example.rabbithell.domain.item.enums.Rarity;
import com.example.rabbithell.domain.shop.entity.Shop;

public record ItemRequest(
    Shop shop,
    String name,
    ItemType itemType,
    Rarity rarity,
    Long price,
    Long attack,
    Long weight,
    Integer durability
) {}
