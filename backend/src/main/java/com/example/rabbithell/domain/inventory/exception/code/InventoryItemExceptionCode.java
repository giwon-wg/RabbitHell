package com.example.rabbithell.domain.inventory.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InventoryItemExceptionCode {

	INVENTORY_ITEM_NOT_FOUND(false, HttpStatus.NOT_FOUND, "아이템이 존재하지 않습니다."),
	USER_MISMATCH(false, HttpStatus.BAD_REQUEST, "나의 아이템이 아닙니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;

}
