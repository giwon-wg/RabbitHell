package com.example.rabbithell.domain.stigma.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum StigmaExceptionCode {
    STIGMA_NOT_FOUND(false, HttpStatus.NOT_FOUND, "스티그마를 찾을 수 없습니다."),
    ;

    private final boolean success;
    private final HttpStatus status;
    private final String message;
}
