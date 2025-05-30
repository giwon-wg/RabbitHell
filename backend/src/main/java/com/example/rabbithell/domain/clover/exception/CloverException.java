package com.example.rabbithell.domain.clover.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.clover.exception.code.CloverExceptionCode;

import lombok.Getter;

@Getter
public class CloverException extends BaseException {

	private final CloverExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String message;

	public CloverException(CloverExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}

}
