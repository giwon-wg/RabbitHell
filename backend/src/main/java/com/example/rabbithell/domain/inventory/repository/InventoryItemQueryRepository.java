package com.example.rabbithell.domain.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.rabbithell.domain.inventory.dto.response.EquipResponse;
import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.enums.Slot;

public interface InventoryItemQueryRepository {

	EquipResponse findEquipmentStatusByCharacter(Long characterId);

	Page<InventoryItem> findEquipableItemBySlot(Inventory inventory, Slot slot, Pageable pageable);

	Long findByCharacterAndSlot(Long characterId, Slot slot);

}
