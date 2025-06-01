package com.example.rabbithell.domain.skill.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.domain.skill.exception.code.SkillExceptionCode;

import lombok.Getter;

@Getter
public class SkillException extends RuntimeException {
	private final SkillExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String message;

	public SkillException(SkillExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}
}
