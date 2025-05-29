package com.example.rabbithell.domain.battle.dto.response;

import java.util.Set;

import com.example.rabbithell.domain.battle.type.BattleFieldType;

public record GetBattleFieldsResponse(
    Set<BattleFieldType> unlockedRareMaps
) {
}
