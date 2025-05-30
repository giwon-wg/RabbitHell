package com.example.rabbithell.domain.battle.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.battle.exception.code.BattleFieldExceptionCode;

import lombok.Getter;

@Getter
public class BattleFieldException extends BaseException {
    private final BattleFieldExceptionCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    public BattleFieldException(BattleFieldExceptionCode errorCode) {
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }
}
