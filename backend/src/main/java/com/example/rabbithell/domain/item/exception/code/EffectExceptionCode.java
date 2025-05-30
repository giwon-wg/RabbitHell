package com.example.rabbithell.domain.item.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EffectExceptionCode {

	NO_SUCH_EFFECT(false, HttpStatus.NOT_FOUND, "효과가 존재하지 않습니다."),
	;

	private final boolean success;
	private final HttpStatus status;
	private final String message;

}
