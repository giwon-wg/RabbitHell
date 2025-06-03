package com.example.rabbithell.domain.inventory.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InventoryExceptionCode {

	USER_MISMATCH(false, HttpStatus.BAD_REQUEST, "나의 인벤토리가 아닙니다."),
	AMOUNT_TOO_SMALL(false, HttpStatus.BAD_REQUEST, "용량 증가는 1 이상이어야 합니다."),
	INVENTORY_NOT_FOUND(false, HttpStatus.BAD_REQUEST, "인벤토리를 찾을 수 없습니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;

}
