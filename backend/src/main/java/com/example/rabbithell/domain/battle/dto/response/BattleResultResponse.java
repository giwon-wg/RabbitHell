package com.example.rabbithell.domain.battle.dto.response;

import java.util.List;
import java.util.Set;

import com.example.rabbithell.domain.battle.enums.BattleResult;
import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.item.entity.Item;
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
	List<Integer> jobSkillPoints,

	Set<BattleFieldType> battleFieldTypes,
	List<InventoryItem> weapon,
	List<InventoryItem> armor,
	List<InventoryItem> accessory,
	List<Integer> playerAttack,
	List<Integer> playerDefense,
	List<Integer> playerSpeed,
	int monsterAttack,
	int monsterDefense,
	int monsterSpeed,
	BattleResult battleResult,
	String battleLog,
	Item earnedItem,
	int usedPotionHp,
	int usedPotionMp
) {

}
