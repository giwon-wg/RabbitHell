package com.example.rabbithell.domain.battle.postProcess.command;

import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.item.entity.Item;
import com.example.rabbithell.domain.monster.entity.DropRate;
import com.example.rabbithell.domain.monster.entity.Monster;
import com.example.rabbithell.domain.util.battleLogic.Battle;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ItemDropCommand implements BattleRewardCommand {

	private final List<DropRate> dropRates;
	private final BattleFieldType battleFieldType;
	private Item item;

	public ItemDropCommand(List<DropRate> dropRates, BattleFieldType battleFieldType){
		this.dropRates = dropRates;
		this.battleFieldType = battleFieldType;
	}

	@Override
	public void execute(){


	}
}
