package com.example.rabbithell.domain.shop.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShopExceptionCode {

	NO_SUCH_SHOP(false, HttpStatus.NOT_FOUND, "상점이 존재하지 않습니다."),
	;

	private final boolean success;
	private final HttpStatus status;
	private final String message;

}
