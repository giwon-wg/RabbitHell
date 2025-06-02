package com.example.rabbithell.domain.inventory.repository;

import com.example.rabbithell.domain.inventory.dto.response.EquipResponse;

public interface InventoryItemQueryRepository {

	EquipResponse findEquipmentStatusByCharacterId(Long characterId);

}
