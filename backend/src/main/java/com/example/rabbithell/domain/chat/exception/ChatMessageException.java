package com.example.rabbithell.domain.chat.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.village.exception.code.VillageExceptionCode;

import lombok.Getter;

@Getter
public class ChatMessageException extends BaseException {

    private final ChatMessageExceptionCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    public ChatMessageException( ChatMessageExceptionCode errorCode) {
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }

}
