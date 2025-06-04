package com.example.rabbithell.domain.pawcard.enums;

public enum CardEmblem {
	SPADE, HEART, DIAMOND, CLUB;

	public String getDisplayName() {
		return switch (this) {
			case SPADE -> "스페이드";
			case HEART -> "하트";
			case DIAMOND -> "다이아몬드";
			case CLUB -> "클로버";
		};
	}
}
