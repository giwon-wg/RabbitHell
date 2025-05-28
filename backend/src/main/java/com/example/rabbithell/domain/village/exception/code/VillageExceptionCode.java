package com.example.rabbithell.domain.village.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VillageExceptionCode {
    ERROR_CODE_NAME(false, HttpStatus.NOT_FOUND, "에러 메시지"),
    ;

    private final boolean success;
    private final HttpStatus status;
    private final String message;
}
