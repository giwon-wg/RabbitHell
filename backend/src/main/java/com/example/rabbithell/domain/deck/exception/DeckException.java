package com.example.rabbithell.domain.deck.exception;

import lombok.Getter;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.deck.exception.code.DeckExceptionCode;

@Getter
public class DeckException extends BaseException {

    private final DeckExceptionCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    public DeckException(DeckExceptionCode errorCode) {
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }
}
