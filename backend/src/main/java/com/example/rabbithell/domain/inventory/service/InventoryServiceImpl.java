package com.example.rabbithell.domain.inventory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.inventory.dto.response.InventoryResponse;
import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional
    @Override
    public InventoryResponse expandInventory(Long userId, int amount) {
        Inventory inventory = inventoryRepository.findByIdOrElseThrow(userId);
        inventory.expand(amount);
        return InventoryResponse.fromEntity(inventory);
    }

}
