package com.example.rabbithell.domain.skill.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SkillExceptionCode {

	SKILL_NOT_FOUND(false, HttpStatus.NOT_FOUND, "해당 스킬을 찾을 수 없습니다."),
	ALREADY_LEARNED(false, HttpStatus.BAD_REQUEST, "이미 배운 스킬입니다."),
	INVALID_JOB_FOR_SKILL(false, HttpStatus.BAD_REQUEST, "직업이 맞지 않아 스킬을 배울 수 없습니다."),
	INSUFFICIENT_TIER_FOR_SKILL(false, HttpStatus.BAD_REQUEST, "현재 직업 티어로는 해당 스킬을 배울 수 없습니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
