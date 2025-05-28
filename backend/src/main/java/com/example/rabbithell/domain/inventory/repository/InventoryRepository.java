package com.example.rabbithell.domain.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.inventory.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
