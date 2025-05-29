package com.example.rabbithell.domain.character.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CharacterExceptionCode {

    CHARACTER_NOT_FOUND(false, HttpStatus.NOT_FOUND, "해당 ID의 캐릭터를찾을 수 없습니다."),
    ;

    private final boolean success;
    private final HttpStatus status;
    private final String message;

}
