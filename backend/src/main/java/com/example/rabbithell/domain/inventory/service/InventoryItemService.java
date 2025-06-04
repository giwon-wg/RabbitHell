package com.example.rabbithell.domain.inventory.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.inventory.dto.request.EquipRequest;
import com.example.rabbithell.domain.inventory.dto.request.UseRequest;
import com.example.rabbithell.domain.inventory.dto.response.EquipResponse;
import com.example.rabbithell.domain.inventory.dto.response.EquipableItemResponse;
import com.example.rabbithell.domain.inventory.dto.response.InventoryItemResponse;
import com.example.rabbithell.domain.inventory.dto.response.UnequipResponse;
import com.example.rabbithell.domain.inventory.dto.response.UseResponse;
import com.example.rabbithell.domain.inventory.enums.Slot;
import com.example.rabbithell.domain.item.entity.Item;

public interface InventoryItemService {

	InventoryItemResponse getInventoryItemById(Long userId, Long inventoryItemId);

	PageResponse<InventoryItemResponse> getAllInventoryItemsFilterBySlot(Long userId, Slot slot, Pageable pageable);

	PageResponse<EquipableItemResponse> getAllEquipableInventoryItems(Long userId, Pageable pageable);

	EquipResponse getEquippedItemsByCharacter(Long userId, Long characterId);

	List<Item> getEquippedItemsByCharacter(Long characterId);

	EquipResponse equipItem(Long userId, Long inventoryItemId, EquipRequest equipRequest);

	UnequipResponse unequipItem(Long userId, Long inventoryItemId);

	UseResponse useItem(Long userId, Long inventoryItemId, UseRequest useRequest);

	void discardItem(Long userId, Long inventoryItemId);

}
