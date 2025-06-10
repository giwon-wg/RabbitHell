package com.example.rabbithell.domain.pawcard.dto.request;

import com.example.rabbithell.common.effect.enums.StatType;
import com.example.rabbithell.domain.pawcard.enums.CardEmblem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePawCardRequest(

	@Schema(description = "x 퍼센트(x는 양의 정수)", example = "100")
	@NotNull Integer ratioPercent,

	@Schema(description = "설명", example = "클로버 5")
	@NotBlank String description,

	@Schema(description = "카드 번호", example = "5")
	@NotNull Integer cardNumber,

	@Schema(description = "카드 엠블럼", example = "CLUB")
	@NotNull CardEmblem cardEmblem,

	@Schema(description = "적용 효과", example = "ATTACK_UP")
	@NotNull StatType statType
) {
}
