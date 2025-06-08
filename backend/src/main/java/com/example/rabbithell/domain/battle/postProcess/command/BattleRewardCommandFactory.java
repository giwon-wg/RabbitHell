package com.example.rabbithell.domain.battle.postProcess.command;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.monster.entity.DropRate;
import org.springframework.stereotype.Component;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.clover.entity.Clover;

@Component
public class BattleRewardCommandFactory {

	public List<ExpRewardCommand> createExpCommands(List<GameCharacter> team, int earnedExp) {
		return team.stream()
			.map(ch -> new ExpRewardCommand(ch, earnedExp))
			.toList();
	}

	public List<LevelUpCommand> createLevelUpCommands(List<GameCharacter> team, List<ExpRewardCommand> expResults) {
		List<LevelUpCommand> list = new ArrayList<>();
		for (int i = 0; i < team.size(); i++) {
			list.add(new LevelUpCommand(team.get(i), expResults.get(i).getResultExp()));
		}
		return list;
	}

	public List<SkillPointRewardCommand> createSkillPointCommands(List<GameCharacter> team, int earnedSkillPoints) {
		return team.stream()
			.map(ch -> new SkillPointRewardCommand(ch, earnedSkillPoints))
			.toList();
	}

	public List<JobPointRewardCommand> createJobPointCommands(List<GameCharacter> team, int earnedJobPoints) {
		return team.stream()
			.map(ch -> new JobPointRewardCommand(ch, earnedJobPoints))
			.toList();
	}

	public List<StatRewardCommand> createStatRewardCommand(List<GameCharacter> team, List<Integer> levelUps) {
		return IntStream.range(0, team.size())
			.mapToObj(i -> new StatRewardCommand(team.get(i), levelUps.get(i)))
			.toList();
	}

	public CashRewardCommand createCashCommand(Clover clover, long earnedCash) {
		return new CashRewardCommand(clover, earnedCash);
	}

	public RareMapCommand createRareMapCommand(Clover clover, BattleFieldType battleFieldType){
		return new RareMapCommand(clover, battleFieldType);
	}


	public ItemDropCommand createItemDropCommand(List<DropRate> dropRates, BattleFieldType battleFieldType) {
		return new ItemDropCommand(dropRates, battleFieldType);
	}
}
