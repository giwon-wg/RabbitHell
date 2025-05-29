package com.example.rabbithell.domain.shop.dto.response;

import com.example.rabbithell.domain.shop.entity.Shop;

public record ShopResponse(String shopName) {

    public static ShopResponse fromEntity(Shop shop) {
        return new ShopResponse(
            shop.getName()
        );
    }

}
