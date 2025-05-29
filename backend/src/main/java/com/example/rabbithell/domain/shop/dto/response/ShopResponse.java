package com.example.rabbithell.domain.shop.dto.response;

import com.example.rabbithell.domain.shop.entity.Shop;

public record ShopResponse(
    Long shopId,
    Long villageId,
    String shopName
) {

    public static ShopResponse fromEntity(Shop shop) {
        return new ShopResponse(
            shop.getId(),
            shop.getVillage().getId(),
            shop.getName()
        );
    }

}
