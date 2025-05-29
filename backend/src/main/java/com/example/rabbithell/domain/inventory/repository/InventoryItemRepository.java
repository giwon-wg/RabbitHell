package com.example.rabbithell.domain.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.entity.InventoryItemId;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, InventoryItemId> {
}
