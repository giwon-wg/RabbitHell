package com.example.rabbithell.domain.battle.vo;

import java.util.List;
import java.util.Set;

import com.example.rabbithell.domain.battle.type.BattleFieldType;

import lombok.Builder;

@Builder
public record BattleRewardResultVo(
	int earnedExp,
	int earnedSkillPoints,
	long cashDelta,
	long totalCash,
	List<Integer> totalExps,
	List<Integer> levels,
	List<Integer> levelUpAmounts,
	List<Integer> totalSkillPoints,
	List<List<Integer>> increasedStat,
	Set<BattleFieldType> unlockedRareMaps
) {
}
