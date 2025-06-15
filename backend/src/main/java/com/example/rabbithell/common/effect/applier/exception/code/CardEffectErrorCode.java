package com.example.rabbithell.common.effect.applier.exception.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CardEffectErrorCode {
	STAT_NOT_FOUND(false, HttpStatus.NOT_FOUND, "지원하지 않는 StatType입니다.");
	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
