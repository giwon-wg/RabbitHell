package com.example.rabbithell.domain.inventory.repository;

import static com.example.rabbithell.domain.inventory.exception.code.InventoryItemExceptionCode.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.entity.InventoryItemId;
import com.example.rabbithell.domain.inventory.exception.InventoryItemException;

public interface InventoryItemRepository
	extends JpaRepository<InventoryItem, InventoryItemId>, InventoryItemQueryRepository {

	default InventoryItem findByIdOrElseThrow(InventoryItemId id) {
		return findById(id).orElseThrow(() -> new InventoryItemException(INVENTORY_ITEM_NOT_FOUND));
	}

}
