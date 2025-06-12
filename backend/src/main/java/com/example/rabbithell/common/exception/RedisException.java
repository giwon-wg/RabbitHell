package com.example.rabbithell.common.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.code.RedisExceptionCode;

import lombok.Getter;

@Getter
public class RedisException extends BaseException {

	private final RedisExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String message;

	public RedisException(RedisExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}
}
