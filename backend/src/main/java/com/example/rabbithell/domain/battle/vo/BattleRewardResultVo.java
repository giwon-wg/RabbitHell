package com.example.rabbithell.domain.battle.vo;

import java.util.List;

public record BattleRewardResultVo(
	int earnedExp,
	int earnedSkillPoints,
	long cashDelta,
	long totalCash,
	List<Integer> totalExps,
	List<Integer> levels,
	List<Integer> levelUpAmounts,
	List<Integer> totalSkillPoints,
	List<Integer> jobSkillPoints
) {
}
