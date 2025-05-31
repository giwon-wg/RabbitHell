package com.example.rabbithell.domain.clover.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CloverExceptionCode {
	ERROR_CODE_NAME(false, HttpStatus.NOT_FOUND, "에러 메시지"),
	Clover_NOT_FOUND(false, HttpStatus.NOT_FOUND, "원정대를 찾을 수 없습니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
