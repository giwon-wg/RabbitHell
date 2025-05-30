package com.example.rabbithell.domain.inventory.service;

import com.example.rabbithell.domain.inventory.dto.response.InventoryResponse;

public interface InventoryService {

    InventoryResponse expandInventory(Long userId, int amount);

}
