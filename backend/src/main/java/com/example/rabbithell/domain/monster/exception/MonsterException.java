package com.example.rabbithell.domain.monster.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.monster.exception.code.MonsterExceptionCode;
import com.example.rabbithell.domain.village.exception.code.VillageExceptionCode;

import lombok.Getter;

@Getter
public class MonsterException extends BaseException {

    private final MonsterExceptionCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    public MonsterException(MonsterExceptionCode errorCode) {
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }
}
