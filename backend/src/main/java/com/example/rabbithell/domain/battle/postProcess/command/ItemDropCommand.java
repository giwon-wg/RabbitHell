package com.example.rabbithell.domain.battle.postProcess.command;

import java.util.List;

import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.item.entity.Item;
import com.example.rabbithell.domain.monster.entity.DropRate;

public class ItemDropCommand implements BattleRewardCommand {

	private final List<DropRate> dropRates;
	private final BattleFieldType battleFieldType;
	private Item item;

	public ItemDropCommand(List<DropRate> dropRates, BattleFieldType battleFieldType) {
		this.dropRates = dropRates;
		this.battleFieldType = battleFieldType;
	}

}
