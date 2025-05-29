package com.example.rabbithell.domain.village.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VillageExceptionCode {
    VILLAGE_NOT_CONNECTED(false, HttpStatus.BAD_REQUEST, "인접하지 않은 마을로는 이동할 수 없습니다."),
    BELOW_ZERO(false, HttpStatus.BAD_REQUEST, "음수 값이 입력되었습니다."),
    NOT_ENOUGH_MONEY(false, HttpStatus.BAD_REQUEST, "돈이 부족합니다."),
    ;

    private final boolean success;
    private final HttpStatus status;
    private final String message;
}
