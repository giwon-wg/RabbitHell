package com.example.rabbithell.domain.character.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.character.exception.code.CharacterExceptionCode;

import lombok.Getter;

@Getter
public class CharacterException extends BaseException {

	private final CharacterExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String message;

	public CharacterException(CharacterExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}

}
