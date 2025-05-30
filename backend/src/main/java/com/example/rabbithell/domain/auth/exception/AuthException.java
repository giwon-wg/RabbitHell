package com.example.rabbithell.domain.auth.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.auth.exception.code.AuthExceptionCode;

import lombok.Getter;

@Getter
public class AuthException extends BaseException {

	private final AuthExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String message;

	public AuthException(AuthExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}

}
