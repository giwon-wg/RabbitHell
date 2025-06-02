package com.example.rabbithell.domain.inventory.service;

import org.springframework.data.domain.Pageable;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.inventory.dto.request.EquipRequest;
import com.example.rabbithell.domain.inventory.dto.response.EquipResponse;
import com.example.rabbithell.domain.inventory.dto.response.InventoryItemResponse;
import com.example.rabbithell.domain.inventory.dto.response.UnequipResponse;
import com.example.rabbithell.domain.inventory.dto.response.UseResponse;

public interface InventoryItemService {

	InventoryItemResponse getInventoryItemById(Long userId, Long inventoryItemId);

	PageResponse<InventoryItemResponse> getAllInventoryItems(Pageable pageable);

	EquipResponse equipItem(Long userId, Long itemId, EquipRequest equipRequest);

	UnequipResponse unequipItem(Long userId, Long inventoryItemId);

	UseResponse useItem(Long userId, Long inventoryItemId);

}
