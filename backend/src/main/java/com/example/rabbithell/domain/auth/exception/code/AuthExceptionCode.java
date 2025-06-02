package com.example.rabbithell.domain.auth.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionCode {
	ERROR_CODE_NAME(false, HttpStatus.NOT_FOUND, "에러 메시지"),
	USER_NOT_FOUND(false, HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
	USER_MISS_MATCH(false, HttpStatus.BAD_REQUEST, "작성자와 요청자가 다릅니다."),
	DUPLICATED_EMAIL(false, HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
	DUPLICATED_CLOVER_NAME(false, HttpStatus.CONFLICT, "이미 사용 중인 원정대명입니다."),
	INVALID_PASSWORD(false, HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	INVALID_REFRESH_TOKEN(false, HttpStatus.BAD_REQUEST, "유효하지 않은 리프레시 토큰입니다."),
	REFRESH_TOKEN_NOT_FOUND(false, HttpStatus.NOT_FOUND, "리프레시 토큰이 존재하지 않습니다."),
	REFRESH_TOKEN_MISMATCH(false, HttpStatus.UNAUTHORIZED, "리프레시 토큰이 일치하지 않습니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
