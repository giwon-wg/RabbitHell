package com.example.rabbithell.domain.deck.enums;

import lombok.Getter;

@Getter
public enum CardRanking {
	ONE_PAIR(1),
	TWO_PAIR(1),
	THREE_OF_A_KIND(2),
	FOUR_OF_A_KIND(3),
	STRAIGHT(10),
	FLUSH(10),
	STRAIGHT_FLUSH(20),
	ROYAL_STRAIGHT_FLUSH(40);

	private final Integer rankingRatio;

	CardRanking(Integer rankingRatio) {
		this.rankingRatio = rankingRatio;
	}
}
