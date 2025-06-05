package com.example.rabbithell.domain.battle.vo;

import java.util.List;

import com.example.rabbithell.domain.battle.enums.BattleResult;
import com.example.rabbithell.domain.item.entity.Item;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BattleResultVo {
	private BattleResult battleResult;
	private List<Integer> playerHp, playerMp;

	private List<Integer> playerAttack, playerDefense, playerSpeed;
	private List<Item> weapon, armor, accessory;
	private int monsterAttack, monsterDefense, monsterSpeed;

	private String log;

}
