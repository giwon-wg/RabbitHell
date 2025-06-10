package com.example.rabbithell.domain.deck.enums;

public enum RabbitHellSortDirection {
	ASC,
	DESC;

	public boolean isAscending() {
		return this == ASC;
	}
}
