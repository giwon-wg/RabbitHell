package com.example.rabbithell.domain.deck.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeckExceptionCode {

	DECK_NOT_FOUND(false, HttpStatus.NOT_FOUND, "유저 클로버의 해당 덱을 찾을 수 없습니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
