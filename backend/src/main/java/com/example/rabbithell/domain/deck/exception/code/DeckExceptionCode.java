package com.example.rabbithell.domain.deck.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DeckExceptionCode {

    STIGMASOCKET_NOT_FOUND(false, HttpStatus.NOT_FOUND, "스티그마 소켓을 찾을 수 없습니다."),
    ;

    private final boolean success;
    private final HttpStatus status;
    private final String message;
}
