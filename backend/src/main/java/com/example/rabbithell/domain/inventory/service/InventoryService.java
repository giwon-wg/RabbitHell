package com.example.rabbithell.domain.inventory.service;

import com.example.rabbithell.domain.inventory.dto.response.EquipResponse;
import com.example.rabbithell.domain.inventory.dto.response.InventoryResponse;

public interface InventoryService {

	InventoryResponse expandInventory(Long userId, int amount);

	EquipResponse getEquippedItemsByCharacter(Long userId, Long characterId);

}
