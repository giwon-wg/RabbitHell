package com.example.rabbithell.domain.inventory.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InventoryItemExceptionCode {

	INVENTORY_ITEM_NOT_FOUND(false, HttpStatus.NOT_FOUND, "아이템이 존재하지 않습니다."),
	USER_MISMATCH(false, HttpStatus.BAD_REQUEST, "나의 아이템이 아닙니다."),
	NOT_CONSUMABLE(false, HttpStatus.BAD_REQUEST, "사용 가능한 아이템이 아닙니다."),
	NOT_ENOUGH_DURABILITY(false, HttpStatus.BAD_REQUEST, "내구도가 부족합니다."),
	NOT_HIDDEN(false, HttpStatus.BAD_REQUEST, "감정이 필요없는 아이템입니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;

}
