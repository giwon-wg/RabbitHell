package com.example.rabbithell.domain.stigma.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

public record UpdateStigmaRequest(
    String name,

    @DecimalMin("0.0000")
    @DecimalMax("1.0000")
    @Digits(integer = 1, fraction = 4)
    BigDecimal ratio,

    String description) {
}
