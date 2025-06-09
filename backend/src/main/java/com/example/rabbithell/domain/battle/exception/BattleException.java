package com.example.rabbithell.domain.battle.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.battle.exception.code.BattleExceptionCode;

import lombok.Getter;

@Getter
public class BattleException extends BaseException {
	private final BattleExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String message;

	public BattleException(BattleExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}
}
