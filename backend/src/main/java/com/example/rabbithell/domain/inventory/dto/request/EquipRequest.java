package com.example.rabbithell.domain.inventory.dto.request;

import com.example.rabbithell.domain.inventory.enums.Slot;

public record EquipRequest(
	Long characterId,
	Slot slot
) {}
