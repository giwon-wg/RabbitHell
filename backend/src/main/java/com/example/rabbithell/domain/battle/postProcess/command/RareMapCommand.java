package com.example.rabbithell.domain.battle.postProcess.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.clover.entity.Clover;

import lombok.Getter;

@Getter
public class RareMapCommand implements BattleRewardCommand {

	private final BattleFieldType curBattleFieldType;
	private List<BattleFieldType> rareMaps;

	public RareMapCommand(BattleFieldType battleFieldType) {
		this.curBattleFieldType = battleFieldType;
	}

	@Override
	public void execute(Clover clover) {

		Random random = new Random();

		List<BattleFieldType> candidates = Arrays.stream(BattleFieldType.values())
			.filter(BattleFieldType::isRare)
			.filter(b ->
				b.getLevel() == curBattleFieldType.getLevel() || b.name().contains("DIM_CRACK")
			)
			.toList();

		if (curBattleFieldType == BattleFieldType.DIM_CRACK) {
			candidates.add(BattleFieldType.TWILIGHT_CRACK);
		} else if (curBattleFieldType == BattleFieldType.TWILIGHT_CRACK) {
			candidates.add(BattleFieldType.ETHER_CRACK);
		} else if (curBattleFieldType == BattleFieldType.ETHER_CRACK) {
			candidates.add(BattleFieldType.NEXUS_CRACK);
		} else if (curBattleFieldType == BattleFieldType.NEXUS_CRACK) {
			candidates.add(BattleFieldType.DREAM_CRACK);
		} else if (curBattleFieldType == BattleFieldType.DREAM_CRACK) {
			candidates.add(BattleFieldType.CENTER_CRACK);
		}

		List<BattleFieldType> eligible = new ArrayList<>();
		for (BattleFieldType candidate : candidates) {
			if (random.nextDouble() < candidate.getAppearanceRate()) {
				eligible.add(candidate);
			}
		}

		if (eligible.isEmpty())
			return;
		for (BattleFieldType map : eligible) {
			clover.unlockRareMap(map);
		}
	}
}
