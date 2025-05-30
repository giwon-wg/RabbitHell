package com.example.rabbithell.domain.monster.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MonsterExceptionCode {
    MONSTER_NOT_FOUND(false, HttpStatus.NOT_FOUND, "몬스터가 존재하지 않습니다."),
    NO_MONSTER_ON_FIELD(false, HttpStatus.BAD_REQUEST, "해당 필드에 존재하지 않는 몬스터입니다.");

    private final boolean success;
    private final HttpStatus status;
    private final String message;
}
