package com.example.rabbithell.common.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisExceptionCode {
	REDIS_CONNECTION_FAIL(false, HttpStatus.INTERNAL_SERVER_ERROR, "Redis 연결 실패"),
	REDIS_SERIALIZATION_FAIL(false, HttpStatus.INTERNAL_SERVER_ERROR, "Redis 직렬화 실패"),
	REDIS_DESERIALIZATION_FAIL(false, HttpStatus.INTERNAL_SERVER_ERROR, "Redis 역직렬화 실패"),
	REDIS_DATA_ACCESS_FAIL(false, HttpStatus.INTERNAL_SERVER_ERROR, "Redis 처리 중 시스템 예외 발생"),
	REDIS_ARGUMENT_ERROR(false, HttpStatus.BAD_REQUEST, "Redis 요청에 잘못된 인자가 포함되어 있습니다."),
	REDIS_JSON_PARSE_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR, "Redis JSON 파싱에 실패했습니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
