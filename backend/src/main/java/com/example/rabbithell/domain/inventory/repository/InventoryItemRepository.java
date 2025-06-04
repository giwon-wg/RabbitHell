package com.example.rabbithell.domain.inventory.repository;

import static com.example.rabbithell.domain.inventory.exception.code.InventoryItemExceptionCode.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.enums.Slot;
import com.example.rabbithell.domain.inventory.exception.InventoryItemException;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long>, InventoryItemQueryRepository {

	default InventoryItem findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new InventoryItemException(INVENTORY_ITEM_NOT_FOUND));
	}

	default InventoryItem findByIdAndValidateOwner(Long id, Long userId) {
		InventoryItem inventoryItem = findByIdOrElseThrow(id);

		if (!inventoryItem.getInventory().getClover().getUser().getId().equals(userId)) {
			throw new InventoryItemException(USER_MISMATCH);
		}
		return inventoryItem;
	}

	Page<InventoryItem> findBySlot(Slot slot, Pageable pageable);
}
