package com.example.rabbithell.domain.inventory.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.inventory.exception.code.InventoryExceptionCode;

import lombok.Getter;

@Getter
public class InventoryException extends BaseException {

	private final InventoryExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String message;

	public InventoryException(InventoryExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}

}
