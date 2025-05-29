package com.example.rabbithell.domain.kingdom.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.kingdom.exception.code.KingdomExceptionCode;
import com.example.rabbithell.domain.village.exception.code.VillageExceptionCode;

import lombok.Getter;

@Getter
public class KingdomException extends BaseException {
    private final KingdomExceptionCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    public KingdomException(KingdomExceptionCode errorCode) {
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }
}
