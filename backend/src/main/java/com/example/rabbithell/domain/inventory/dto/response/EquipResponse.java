package com.example.rabbithell.domain.inventory.dto.response;

import java.util.List;

public record EquipResponse(
	List<EquippedItem> equippedItems
) {
}
