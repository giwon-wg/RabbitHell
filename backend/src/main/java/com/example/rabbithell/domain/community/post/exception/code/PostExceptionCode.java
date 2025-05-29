package com.example.rabbithell.domain.community.post.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostExceptionCode {
    ERROR_CODE_NAME(false, HttpStatus.NOT_FOUND, "에러 메시지"),
    USER_NOT_FOUND(false, HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    POST_NOT_FOUND(false, HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    USER_MISMATCH(false, HttpStatus.BAD_REQUEST, "작성자와 요청자가 다릅니다."),
    ;

    private final boolean success;
    private final HttpStatus status;
    private final String message;
}
