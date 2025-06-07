package com.example.rabbithell.domain.chat.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatMessageExceptionCode {
	//메시지 관련
	NULL_MESSAGE(false, HttpStatus.BAD_REQUEST, "메시지가 존재하지 않습니다."),
	INVALID_PAYLOAD(false, HttpStatus.BAD_REQUEST, "잘못된 메시지 형식입니다."),
	MESSAGE_PROCESSING_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR, "메시지 처리 중 오류가 발생했습니다."),
	COOLDOWN_NOT_FINISHED(false, HttpStatus.TOO_MANY_REQUESTS, "메시지 전송 쿨타임이 아직 끝나지 않았습니다."),
	//권한관련
	UNAUTHORIZED(false, HttpStatus.UNAUTHORIZED,"권한이 없습니다"),
	NO_ADMIN_PRIVILEGES(false, HttpStatus.FORBIDDEN, "관리자 권한이 없습니다");

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
