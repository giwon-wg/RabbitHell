package com.example.rabbithell.domain.battle.vo;

import java.util.List;

import com.example.rabbithell.domain.battle.entity.BattleField;
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
	List<Integer> jobSkillPoints,
	List<List<Integer>> increasedStat,
	List<BattleFieldType> unlockedRareMaps
) {
}
