package com.example.rabbithell.domain.inventory.dto.response;

import com.example.rabbithell.domain.inventory.entity.Inventory;

public record InventoryResponse(
    Long inventoryId,
    Integer capacity
) {
    public static InventoryResponse fromEntity(Inventory inventory) {
        return new InventoryResponse(
            inventory.getId(),
            inventory.getCapacity()
        );
    }
}
