package com.example.rabbithell.domain.shop.service;

import org.springframework.data.domain.Pageable;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.shop.dto.request.AddItemRequest;
import com.example.rabbithell.domain.shop.dto.request.ShopRequest;
import com.example.rabbithell.domain.shop.dto.response.BuyItemResponse;
import com.example.rabbithell.domain.shop.dto.response.SellItemResponse;
import com.example.rabbithell.domain.shop.dto.response.ShopItemResponse;
import com.example.rabbithell.domain.shop.dto.response.ShopResponse;

import jakarta.validation.Valid;

public interface ShopService {

	ShopResponse getShopById(Long shopId);

	ShopResponse createShop(ShopRequest shopRequest);

	ShopResponse updateShop(Long shopId, @Valid ShopRequest shopRequest);

	void deleteShop(Long shopId);

	ShopItemResponse addItem(Long shopId, AddItemRequest addItemRequest);

	ShopItemResponse getShopItem(Long itemId);

	PageResponse<ShopItemResponse> getAllShopItems(Long shopId, Pageable pageable);

	BuyItemResponse buyItem(Long userId, Long itemId, int quantity);

	SellItemResponse sellItem(Long inventoryItemId);

}
