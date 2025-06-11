package com.example.rabbithell.domain.inventory.repository;

import static com.example.rabbithell.domain.inventory.exception.code.InventoryItemExceptionCode.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.enums.Slot;
import com.example.rabbithell.domain.inventory.exception.InventoryItemException;
import com.example.rabbithell.domain.item.dto.response.ItemCountResponse;

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

	List<InventoryItem> findByCharacter_Id(Long characterId);

	int countByInventory_Id(Long inventoryId);

	@Query("SELECT new com.example.rabbithell.domain.item.dto.response.ItemCountResponse(i.item.id, count(i))"
		+ "FROM InventoryItem i GROUP BY i.item.id")
	List<ItemCountResponse> countInventoryItemsByItem_Id();

}
