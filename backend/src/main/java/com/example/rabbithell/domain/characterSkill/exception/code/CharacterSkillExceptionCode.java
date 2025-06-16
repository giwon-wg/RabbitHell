package com.example.rabbithell.domain.characterSkill.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CharacterSkillExceptionCode {

	NOT_LEARNED_YET(false, HttpStatus.NOT_FOUND, "아직 배우지 않은 스킬입니다."),
	ALREADY_EQUIPPED(false, HttpStatus.BAD_REQUEST, "이미 장착된 스킬입니다."),
	ALREADY_UNEQUIPPED(false, HttpStatus.BAD_REQUEST, "이미 장착 해제된 스킬입니다."),
	NOT_EQUIPPED(false, HttpStatus.BAD_REQUEST, "장착되어 있지 않은 스킬입니다."),
	MAX_EQUIPPED_SKILLS_REACHED(false, HttpStatus.BAD_REQUEST, "최대 장착 가능한 스킬 수를 초과했습니다."),
	SKILL_NOT_FOUND(false, HttpStatus.NOT_FOUND, "스킬을 찾을 수 없습니다."),
	CHARACTER_NOT_FOUND(false, HttpStatus.NOT_FOUND, "캐릭터를 찾을 수 없습니다."),
	EQUIP_SKILL_BELONGS_TO_OTHER_CHARACTER(false, HttpStatus.BAD_REQUEST, "해당 스킬은 다른 캐릭터의 스킬입니다."),
	INVALID_EQUIP_TYPE(false, HttpStatus.BAD_REQUEST, "해당 슬롯에 장착할 수 없는 스킬 타입입니다."),
	ALREADY_EQUIPPED_IN_OTHER_SLOT(false, HttpStatus.BAD_REQUEST, "이미 장착되어있는 스킬입니다."),
	MAX_ACTIVE_SLOTS_REACHED(false, HttpStatus.BAD_REQUEST, "액티브 스킬 슬롯이 최대치입니다."),
	MAX_PASSIVE_SLOTS_REACHED(false, HttpStatus.BAD_REQUEST, "패시브 스킬 슬롯이 최대치입니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;

}
