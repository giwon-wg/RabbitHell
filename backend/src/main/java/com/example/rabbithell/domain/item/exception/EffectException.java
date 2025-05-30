package com.example.rabbithell.domain.item.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.item.exception.code.EffectExceptionCode;

import lombok.Getter;

@Getter
public class EffectException extends BaseException {

	private final EffectExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String message;

	public EffectException(EffectExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}

}
