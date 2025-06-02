package com.example.rabbithell.domain.battle.dto.request;

import com.example.rabbithell.domain.battle.type.BattleFieldType;

import jakarta.validation.constraints.NotNull;

public record DoBattleRequest(
    @NotNull Long characterId,
    @NotNull BattleFieldType battleFieldType
) {
}
