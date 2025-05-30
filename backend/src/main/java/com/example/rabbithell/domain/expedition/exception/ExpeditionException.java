package com.example.rabbithell.domain.expedition.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.expedition.exception.code.ExpeditionExceptionCode;

import lombok.Getter;

@Getter
public class ExpeditionException extends BaseException {

	private final ExpeditionExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String message;

	public ExpeditionException(ExpeditionExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}

}
