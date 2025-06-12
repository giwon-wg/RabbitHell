package com.example.rabbithell.domain.battle.dto.response;

import java.util.List;

public record GetBattleFieldsResponse(
	List<BattleFieldDto> unlockedRareMaps
) {
}
