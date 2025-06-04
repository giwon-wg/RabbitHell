package com.example.rabbithell.domain.deck.enums;

import com.example.rabbithell.domain.deck.entity.QDeck;
import com.example.rabbithell.domain.pawcard.entity.QPawCard;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;

public enum DeckSortBy {
	CREATED_AT(QDeck.deck.createdAt),
	CARD_NUMBER(QPawCard.pawCard.cardNumber),
	CARD_EMBLEM(QPawCard.pawCard.cardEmblem),
	RATIO(QPawCard.pawCard.ratio);

	private final ComparableExpressionBase<?> expression;

	DeckSortBy(ComparableExpressionBase<?> expression) {
		this.expression = expression;
	}

	public OrderSpecifier<?> toOrderSpecifier(boolean ascending) {
		return ascending ? expression.asc() : expression.desc();
	}
}
