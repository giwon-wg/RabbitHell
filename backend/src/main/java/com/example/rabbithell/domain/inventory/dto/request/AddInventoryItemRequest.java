package com.example.rabbithell.domain.inventory.dto.request;

public record AddInventoryItemRequest(
	Long cloverId,
	Long itemId
) {
}
