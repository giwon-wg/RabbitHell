package com.example.rabbithell.domain.pawcard.dto.request;

import com.example.rabbithell.common.effect.enums.DomainType;
import com.example.rabbithell.common.effect.enums.StatCategory;
import com.example.rabbithell.common.effect.enums.StatType;
import com.example.rabbithell.domain.pawcard.enums.CardEmblem;

import io.swagger.v3.oas.annotations.media.Schema;

public record PawCardCond(
	@Schema(description = "삭제 여부 / ex) 삭제 = true", example = "false")
	Boolean isDeleted,

	@Schema(description = "카드 번호", example = "5")
	Integer cardNumber,

	@Schema(description = "카드 엠블럼", example = "CLUB")
	CardEmblem cardEmblem,

	@Schema(description = "적용 효과", example = "ATTACK_UP")
	StatType StatType,

	@Schema(description = "적용 카테고리", example = "BATTLE_STAT")
	StatCategory statCategory,

	@Schema(description = "적용 대상", example = "GAME_CHARACTER")
	DomainType domainType
) {
}
