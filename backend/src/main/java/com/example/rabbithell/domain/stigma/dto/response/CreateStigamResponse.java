package com.example.rabbithell.domain.stigma.dto.response;

import com.example.rabbithell.domain.stigma.entity.Stigma;

public record CreateStigamResponse(Long stigmaId) {

    public static CreateStigamResponse from (Stigma stigma) {
        return new CreateStigamResponse(stigma.getId());
    }
}
