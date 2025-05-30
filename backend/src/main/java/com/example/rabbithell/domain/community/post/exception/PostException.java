package com.example.rabbithell.domain.community.post.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.community.post.exception.code.PostExceptionCode;

import lombok.Getter;

@Getter
public class PostException extends BaseException {

	private final PostExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String message;

	public PostException(PostExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}

}
