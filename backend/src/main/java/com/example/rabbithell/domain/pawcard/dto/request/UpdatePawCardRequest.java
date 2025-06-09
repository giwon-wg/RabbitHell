package com.example.rabbithell.domain.pawcard.dto.request;

import com.example.rabbithell.common.effect.enums.StatType;
import com.example.rabbithell.domain.pawcard.enums.CardEmblem;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdatePawCardRequest(

	@Schema(description = "x 퍼센트(x는 양의 정수)", example = "100")
	Integer ratioPercent,

	@Schema(description = "설명", example = "내안의 흑염룡이...")
	String description,

	@Schema(description = "카드 번호", example = "5")
	Integer cardNumber,

	@Schema(description = "카드 엠블럼", example = "CLUB")
	CardEmblem cardEmblem,

	@Schema(description = "적용 효과", example = "ATTACK_UP")
	StatType statType
) {
}
