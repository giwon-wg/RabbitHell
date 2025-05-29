package com.example.rabbithell.domain.village.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.village.exception.code.VillageExceptionCode;

import lombok.Getter;

@Getter
public class VillageException extends BaseException {

    private final VillageExceptionCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    public VillageException(VillageExceptionCode errorCode) {
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }

}
