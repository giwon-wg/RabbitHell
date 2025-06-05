package com.example.rabbithell.domain.inventory.repository;

import static com.example.rabbithell.domain.inventory.exception.code.InventoryItemExceptionCode.*;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.enums.Slot;
import com.example.rabbithell.domain.inventory.exception.InventoryItemException;
import com.example.rabbithell.domain.item.enums.ItemType;

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

	Page<InventoryItem> findByInventoryAndSlot(Inventory inventory, Slot slot, Pageable pageable);

	Page<InventoryItem> findByInventoryAndItem_ItemTypeIn(Inventory inventory, List<ItemType> itemTypes,
		Pageable pageable);

	List<InventoryItem> findByCharacter_Id(Long characterId);

	Page<InventoryItem> findByInventoryAndItem_ItemTypeInAndSlot(Inventory inventory, Collection<ItemType> itemItemTypes, Slot slot, Pageable pageable);

	Page<InventoryItem> findByInventoryAndItem_ItemTypeInAndSlotAndCharacterIsNullAndSlotIsNull(Inventory inventory, Collection<ItemType> itemItemTypes, Slot slot, GameCharacter character,
		Slot slot1, Pageable pageable);
}
