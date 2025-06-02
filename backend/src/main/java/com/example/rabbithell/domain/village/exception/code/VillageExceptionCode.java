package com.example.rabbithell.domain.village.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VillageExceptionCode {
	VILLAGE_NOT_CONNECTED(false, HttpStatus.BAD_REQUEST, "인접하지 않은 마을로는 이동할 수 없습니다."),
	VILLAGE_NOT_FOUND(false, HttpStatus.NOT_FOUND, "존재하지 않는 마을입니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
