package com.example.rabbithell.domain.job.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.domain.job.exception.code.JobExceptionCode;

import lombok.Getter;

@Getter
public class JobException extends RuntimeException {

	private final JobExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String message;

	public JobException(JobExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}

}
