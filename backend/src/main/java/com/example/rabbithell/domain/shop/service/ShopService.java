package com.example.rabbithell.domain.shop.service;

import com.example.rabbithell.domain.shop.dto.request.ShopRequest;
import com.example.rabbithell.domain.shop.dto.response.ShopResponse;
import jakarta.validation.Valid;

public interface ShopService {

    ShopResponse findShopById(Long shopId);

    ShopResponse createShop(ShopRequest shopRequest);

    ShopResponse updateShop(Long shopId, @Valid ShopRequest shopRequest);

}
