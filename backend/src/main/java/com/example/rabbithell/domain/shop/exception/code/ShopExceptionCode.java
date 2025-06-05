package com.example.rabbithell.domain.shop.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShopExceptionCode {

	SHOP_NOT_FOUND(false, HttpStatus.NOT_FOUND, "상점이 존재하지 않습니다."),
	INVENTORY_FULL(false, HttpStatus.BAD_REQUEST, "인벤토리 용량이 부족합니다."),
	NOT_ENOUGH_CASH(false, HttpStatus.BAD_REQUEST, "돈이 부족합니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;

}
