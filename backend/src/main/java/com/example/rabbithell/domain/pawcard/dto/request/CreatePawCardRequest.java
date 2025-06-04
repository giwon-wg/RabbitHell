package com.example.rabbithell.domain.pawcard.dto.request;

import java.math.BigDecimal;

import com.example.rabbithell.common.effect.enums.StatType;
import com.example.rabbithell.domain.pawcard.enums.CardEmblem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePawCardRequest(

	@Schema(description = "0~1까지 소숫점 4자리수", example = "0.1234")
	@DecimalMin("0.0000")
	@DecimalMax("1.0000")
	@Digits(integer = 1, fraction = 4)
	@NotNull BigDecimal ratio,

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
