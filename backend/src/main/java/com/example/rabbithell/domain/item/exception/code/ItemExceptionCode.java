package com.example.rabbithell.domain.item.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemExceptionCode {

	NO_SUCH_ITEM(false, HttpStatus.NOT_FOUND, "아이템이 존재하지 않습니다."),
	;

	private final boolean success;
	private final HttpStatus status;
	private final String message;

}
