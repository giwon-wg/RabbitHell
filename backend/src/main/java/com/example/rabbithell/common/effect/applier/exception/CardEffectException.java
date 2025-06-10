package com.example.rabbithell.common.effect.applier.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.effect.applier.exception.code.CardEffectErrorCode;
import com.example.rabbithell.common.exception.BaseException;

import lombok.Getter;

@Getter
public class CardEffectException extends BaseException {

	private final CardEffectErrorCode errorCode;
	private final HttpStatus httpStatus;
	private final String message;

	public CardEffectException(CardEffectErrorCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}
}
