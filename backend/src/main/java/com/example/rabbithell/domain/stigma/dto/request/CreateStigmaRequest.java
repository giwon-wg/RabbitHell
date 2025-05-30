package com.example.rabbithell.domain.stigma.dto.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateStigmaRequest(
	@Schema(description = "스티그마 이름", example = "끠끠신의 눈물")
	@NotBlank String name,

	@Schema(description = "0~1까지 소숫점 4자리수", example = "0.1234")
	@DecimalMin("0.0000")
	@DecimalMax("1.0000")
	@Digits(integer = 1, fraction = 4)
	@NotNull BigDecimal ratio,

	@Schema(description = "설명", example = "끠끠신의 가호가...")
	@NotBlank String description) {
}
