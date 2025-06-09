package com.example.rabbithell.domain.battle.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BattleExceptionCode {
	BATTLEFIELD_NOT_FOUND(false, HttpStatus.NOT_FOUND, "필드가 존재하지 않습니다."),
	UNACCESSIBLE_FIELD(false, HttpStatus.BAD_REQUEST, "접근할 수 없는 필드입니다."),
	;

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
