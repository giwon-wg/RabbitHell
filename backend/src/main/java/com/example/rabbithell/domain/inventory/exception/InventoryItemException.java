package com.example.rabbithell.domain.inventory.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.inventory.exception.code.InventoryItemExceptionCode;

import lombok.Getter;

@Getter
public class InventoryItemException extends BaseException {

	private final InventoryItemExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String message;

	public InventoryItemException(InventoryItemExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}

}
