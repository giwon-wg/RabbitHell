package com.example.rabbithell.domain.village.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SaveMoneyRequest (
    @NotNull Long characterId,

    @NotNull Long saveMoney
){

}
