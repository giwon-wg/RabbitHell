package com.example.rabbithell.domain.battle.dto.request;

import jakarta.validation.constraints.NotNull;

public record GetBattleFieldsRequest(
    @NotNull Long characterId
) {}
