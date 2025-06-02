package com.example.rabbithell.domain.battle.dto.response;

import java.util.List;
import java.util.Set;

import com.example.rabbithell.domain.battle.enums.BattleResult;
import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.item.entity.Item;

import lombok.Builder;

@Builder
public record BattleResultResponse(
	Long cloverId,
	int stamina,

	List<Long> characterIds,
	List<Integer> level,
	List<Integer> earnedExp,
	List<Integer> totalExp,
	int earnedMoney,
	int totalMoney,
	int earnedCash,
	int totalCash,
	List<Integer> earnedSkillPoint,
	List<Integer> totalSkillPoint,

	Set<BattleFieldType> battleFieldTypes,
	List<Item> weapon,
	List<Item> armor,
	List<Item> accessory,
	List<Integer> playerAttack,
	List<Integer> playerDefense,
	List<Integer> playerSpeed,
	List<Integer> monsterAttack,
	List<Integer> monsterDefence,
	List<Integer> monsterSpeed,
	BattleResult battleResult,
	String battleLog,
	Item earnedItem,
	int usedPotionHP,
	int usedPotionMP
) {

}
