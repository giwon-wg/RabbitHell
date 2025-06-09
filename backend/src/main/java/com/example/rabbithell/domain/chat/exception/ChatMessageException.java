package com.example.rabbithell.domain.chat.exception;

import com.example.rabbithell.common.exception.BaseException;

import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
public class ChatMessageException extends BaseException {

	private final ChatMessageExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String message;

	public ChatMessageException(ChatMessageExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}

}
