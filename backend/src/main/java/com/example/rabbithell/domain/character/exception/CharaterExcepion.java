package com.example.rabbithell.domain.character.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.character.exception.code.CharaterExceptionCode;

import lombok.Getter;

@Getter
public class CharaterExcepion extends BaseException {

    private final CharaterExceptionCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    public CharaterExcepion(CharaterExceptionCode errorCode) {
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }

}
