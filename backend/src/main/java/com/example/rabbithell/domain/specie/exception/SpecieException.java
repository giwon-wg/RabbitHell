package com.example.rabbithell.domain.specie.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.specie.exception.code.SpecieExceptionCode;

import lombok.Getter;

@Getter
public class SpecieException extends BaseException {
    private final SpecieExceptionCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    public SpecieException(SpecieExceptionCode errorCode){
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }


}
