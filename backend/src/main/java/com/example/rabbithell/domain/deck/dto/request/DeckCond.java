package com.example.rabbithell.domain.deck.dto.request;

import com.example.rabbithell.domain.deck.enums.DeckSortBy;
import com.example.rabbithell.domain.deck.enums.RabbitHellSortDirection;

public record DeckCond(
	Boolean equippedOnly,
	DeckSortBy sortBy,
	RabbitHellSortDirection sortDirection
) {
}
