package com.example.rabbithell.domain.battle.postProcess.command;

import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.clover.entity.Clover;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class RareMapCommand implements BattleRewardCommand {

	private final Clover clover;
	private final BattleFieldType curBattleFieldType;
	private List<BattleFieldType> rareMaps;


	public RareMapCommand(Clover clover, BattleFieldType battleFieldType) {
		this.clover = clover;
		this.curBattleFieldType = battleFieldType;
	}

	@Override
	public void execute() {

		Random random = new Random();

		List<BattleFieldType> candidates = Arrays.stream(BattleFieldType.values())
			.filter(BattleFieldType::isRare)
			.filter(b ->
				b.getLevel() == curBattleFieldType.getLevel() || b.name().contains("DIM_CRACK")
			)
			.toList();

		if(curBattleFieldType == BattleFieldType.DIM_CRACK){
			candidates.add(BattleFieldType.TWILIGHT_CRACK);
		}else if(curBattleFieldType == BattleFieldType.TWILIGHT_CRACK){
			candidates.add(BattleFieldType.ETHER_CRACK);
		}else if(curBattleFieldType == BattleFieldType.ETHER_CRACK){
			candidates.add(BattleFieldType.NEXUS_CRACK);
		}else if(curBattleFieldType == BattleFieldType.NEXUS_CRACK){
			candidates.add(BattleFieldType.DREAM_CRACK);
		}else if(curBattleFieldType == BattleFieldType.DREAM_CRACK){
			candidates.add(BattleFieldType.CENTER_CRACK);
		}


		List<BattleFieldType> eligible = new ArrayList<>();
		for (BattleFieldType candidate : candidates) {
			if (random.nextDouble() < candidate.getAppearanceRate()) {
				eligible.add(candidate);
			}
		}

		if (eligible.isEmpty()) return;

		this.rareMaps = eligible;
	}

}
