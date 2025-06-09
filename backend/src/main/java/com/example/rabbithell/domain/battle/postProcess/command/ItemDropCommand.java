package com.example.rabbithell.domain.battle.postProcess.command;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.item.entity.Item;
import com.example.rabbithell.domain.monster.entity.DropRate;

import lombok.Getter;

@Getter
public class ItemDropCommand implements BattleRewardCommand {

	private final List<DropRate> dropRates;
	private List<Item> items = new ArrayList<>();

	public ItemDropCommand(List<DropRate> dropRates) {
		this.dropRates = dropRates;
	}

	@Override
	public void execute(Clover clover) {
		Random random = new Random();

		for (DropRate dropRate : dropRates) {
			BigDecimal chance = dropRate.getRate();
			BigDecimal roll = BigDecimal.valueOf(random.nextDouble());

			if (roll.compareTo(chance) < 0) {
				Item item = dropRate.getItem();
				items.add(item);
			}
		}

	}

}
