package com.example.rabbithell.domain.kingdom.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KingdomExceptionCode {
    KINGDOM_NOT_FOUND(false, HttpStatus.NOT_FOUND, "존재하지 않는 왕국입니다."),
    ;

    private final boolean success;
    private final HttpStatus status;
    private final String message;
}
