package com.example.rabbithell.domain.pawcard.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.pawcard.exception.code.PawCardExceptionCode;

import lombok.Getter;

@Getter
public class PawCardException extends BaseException {

	private final PawCardExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String message;

	public PawCardException(PawCardExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}
}
