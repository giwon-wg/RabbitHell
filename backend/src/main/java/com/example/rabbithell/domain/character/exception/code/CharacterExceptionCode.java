package com.example.rabbithell.domain.character.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CharacterExceptionCode {

	CHARACTER_NOT_FOUND(false, HttpStatus.NOT_FOUND, "해당 ID의 캐릭터를찾을 수 없습니다."),
	ACCESS_DENIED(false, HttpStatus.FORBIDDEN, "본인의 캐릭터만 변경할 수 있습니다."),
	LEVEL_IS_LOW(false, HttpStatus.BAD_REQUEST, "전직 최소 레벨은 50 이상입니다."),
	SKILL_POINT_IS_LOW(false, HttpStatus.BAD_REQUEST, "숙련도가 부족하여 전직할 수 없습니다."),
	MAIN_JOB_POINT_IS_LOW(false, HttpStatus.BAD_REQUEST, "직업 포인트가 부족하여 전직할 수 없습니다."),
	SUB_JOB_POINT_IS_LOW(false, HttpStatus.BAD_REQUEST, "타 직업 포인트가 부족하여 전직할 수 없습니다.")
	;

	private final boolean success;
	private final HttpStatus status;
	private final String message;

}
