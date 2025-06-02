package com.example.rabbithell.domain.job.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobExceptionCode {

	JOB_NOT_FOUND(false, HttpStatus.NOT_FOUND, "해당 직업을 찾을 수 없습니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;

}
