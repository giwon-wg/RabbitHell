package com.example.rabbithell.common.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {
	public abstract Enum<?> getErrorCode();

	public abstract HttpStatus getHttpStatus();

	public abstract String getMessage();
}
