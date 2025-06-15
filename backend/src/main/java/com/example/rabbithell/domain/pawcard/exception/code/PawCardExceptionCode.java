package com.example.rabbithell.domain.pawcard.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PawCardExceptionCode {
	PAWCARD_NOT_FOUND(false, HttpStatus.NOT_FOUND, "포카드를 찾을 수 없습니다.");
	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
