package com.example.rabbithell.domain.stigma.exception;

import lombok.Getter;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.stigma.exception.code.StigmaExceptionCode;

@Getter
public class StigmaException extends BaseException {

    private final StigmaExceptionCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    public StigmaException(StigmaExceptionCode errorCode) {
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }
}
