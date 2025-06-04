package com.example.rabbithell.domain.battle.vo;

import java.util.List;

import com.example.rabbithell.domain.battle.enums.BattleResult;
import com.example.rabbithell.domain.item.entity.Item;

import lombok.Builder;

@Builder
public class BattleResultVo {
	private BattleResult battleResult;
	private int usedPotionHp;
	private int usedPotionMp;

	private List<Integer> playerAttack, playerDefense, playerSpeed;
	private List<Item> weapon, armor, accessory;
	private int monsterAttack, monsterDefence, monsterSpeed;

	private String log;

}
