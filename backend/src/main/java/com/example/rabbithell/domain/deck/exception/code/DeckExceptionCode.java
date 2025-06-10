package com.example.rabbithell.domain.deck.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeckExceptionCode {

	DECK_NOT_FOUND(false, HttpStatus.NOT_FOUND, "유저 클로버의 해당 덱을 찾을 수 없습니다."),
	INVALID_DECK_REQUEST(false, HttpStatus.BAD_REQUEST, "DB에 있는 대상이지만, 요청과 매칭되지 않기 때문에 더 이상 처리할 수 없습니다."),
	INVALID_EFFECT_STRUCTURE(false, HttpStatus.INTERNAL_SERVER_ERROR, "EffectDetail이 2개 미만으로 저장 구조가 원할하지 않습니다."),
	PAW_CARD_EFFECT_NOT_FOUND(false, HttpStatus.NOT_FOUND, "PawCardEffect를 찾을 수 없습니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
