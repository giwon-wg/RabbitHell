package com.example.rabbithell.domain.stigma.dto.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

public record UpdateStigmaRequest(
	@Schema(description = "스티그마 이름", example = "흑염룡의 혈흔")
	String name,

    @DecimalMin("0.0000")
    @DecimalMax("1.0000")
    @Digits(integer = 1, fraction = 4)
	@Schema(description = "0~1까지 소숫점 4자리수", example = "0.5678")
	BigDecimal ratio,

	@Schema(description = "설명", example = "내안의 흑염룡이...")
	String description) {
}
