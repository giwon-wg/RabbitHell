package com.example.rabbithell.domain.battle.dto.response;

import java.util.List;
import java.util.Set;

import com.example.rabbithell.domain.battle.enums.BattleResult;
import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.job.entity.Job;

import lombok.Builder;

@Builder
public record BattleResultResponse(
	Long cloverId,
	int stamina,

	List<Long> characterIds,
	List<Integer> level,
	int earnedExp,
	List<Integer> totalExp,
	List<Integer> levelUpAmounts,
	Long lostOrEarnedCash,
	Long totalCash,

	List<Job> jobs,
	int earnedSkillPoint,
	List<Integer> totalSkillPoints,
	List<List<Integer>> increasedStats,

	Set<BattleFieldType> battleFieldTypes,
	List<String> characterNames,
	List<ItemDto> weapon,
	List<ItemDto> armor,
	List<ItemDto> accessory,
	List<Integer> playerHp,
	List<Integer> maxHp,
	List<Integer> playerMp,
	List<Integer> maxMp,
	List<Integer> playerAttack,
	List<Integer> playerDefense,
	List<Integer> playerSpeed,

	String monsterName,
	int monsterHp,
	int monsterMaxHp,
	int monsterAttack,
	int monsterDefense,
	int monsterSpeed,
	BattleResult battleResult,
	String battleLog,
	List<EarnedItemDto> earnedItems,
	int usedPotionHp,
	int usedPotionMp
) {

}
