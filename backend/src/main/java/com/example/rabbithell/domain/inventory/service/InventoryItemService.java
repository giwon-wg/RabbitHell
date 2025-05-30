package com.example.rabbithell.domain.inventory.service;

import org.springframework.data.domain.Pageable;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.inventory.dto.response.InventoryItemResponse;

public interface InventoryItemService {

	InventoryItemResponse getInventoryItemById(Long userId, Long itemId);

	PageResponse<InventoryItemResponse> getAllInventoryItems(Pageable pageable);

}
