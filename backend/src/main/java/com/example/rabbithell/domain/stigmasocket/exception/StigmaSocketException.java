package com.example.rabbithell.domain.stigmasocket.exception;

import lombok.Getter;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.stigmasocket.exception.code.StigmaSocketExceptionCode;

@Getter
public class StigmaSocketException extends BaseException {

    private final StigmaSocketExceptionCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    public StigmaSocketException(StigmaSocketExceptionCode errorCode) {
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }
}
