package com.example.rabbithell.domain.skill.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SkillExceptionCode {

	SKILL_NOT_FOUND(false, HttpStatus.NOT_FOUND, "해당 스킬을 찾을 수 없습니다.")
	;

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
