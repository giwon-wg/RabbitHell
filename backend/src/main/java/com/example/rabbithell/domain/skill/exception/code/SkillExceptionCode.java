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
	INSUFFICIENT_TIER_FOR_SKILL(false, HttpStatus.BAD_REQUEST, "현재 직업 티어로는 해당 스킬을 배울 수 없습니다."),
	NOT_ENOUGH_SKILL_POINTS(false,HttpStatus.BAD_REQUEST,"스킬 학습을 위한 스킬 포인트가 부족합니다."),
	NOT_ENOUGH_SKILL_SLOTS(false,HttpStatus.BAD_REQUEST,"스킬 장착 슬롯이 유효하지 않습니다."),
	SKILL_NOT_OWNED(false, HttpStatus.BAD_REQUEST, "보유하지 않은 스킬입니다."),
	INVALID_EQUIP_SLOT(false, HttpStatus.BAD_REQUEST, "유효하지 않은 스킬 장착 슬롯입니다."),
	ALREADY_EQUIPPED(false, HttpStatus.CONFLICT, "이미 장착된 스킬입니다."),
	NOT_LEARNED(false, HttpStatus.BAD_REQUEST, "아직 배우지 않은 스킬입니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
