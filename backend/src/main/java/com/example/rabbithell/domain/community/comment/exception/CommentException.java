package com.example.rabbithell.domain.community.comment.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.community.comment.exception.code.CommentExceptionCode;

import lombok.Getter;

@Getter
public class CommentException extends BaseException {

	private final CommentExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String message;

	public CommentException(CommentExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}

}
