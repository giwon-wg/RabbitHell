package com.example.rabbithell.domain.shop.dto.request;

public record AddItemRequest(
	Long itemId,
	Long price
) {
}
