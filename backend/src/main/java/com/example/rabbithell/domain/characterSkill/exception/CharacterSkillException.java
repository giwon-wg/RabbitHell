package com.example.rabbithell.domain.characterSkill.exception;

import org.springframework.http.HttpStatus;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.characterSkill.exception.code.CharacterSkillExceptionCode;

import lombok.Getter;

@Getter
public class CharacterSkillException extends BaseException {

	private final CharacterSkillExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String message;

	public CharacterSkillException(CharacterSkillExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}

}
