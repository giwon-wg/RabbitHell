package com.example.rabbithell.domain.shop.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.shop.exception.code.ShopExceptionCode;

import lombok.Getter;

@Getter
public class ShopException extends BaseException {

	private final ShopExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String message;

	public ShopException(ShopExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}

}
