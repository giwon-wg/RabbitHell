package com.example.rabbithell.domain.chat.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatMessageExceptionCode {
	NULL_MESSAGE(false, HttpStatus.BAD_REQUEST, "웹 소켓 메시지가 존재하지 않습니다."),
	INVALID_PAYLOAD(false, HttpStatus.BAD_REQUEST, "잘못된 메시지 형식입니다."),
	MESSAGE_PROCESSING_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR, "메시지 처리 중 오류가 발생했습니다."),
	;

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
