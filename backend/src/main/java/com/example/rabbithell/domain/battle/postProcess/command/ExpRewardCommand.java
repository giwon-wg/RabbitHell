package com.example.rabbithell.domain.battle.postProcess.command;

import com.example.rabbithell.domain.character.entity.GameCharacter;

import lombok.Getter;

@Getter
public class ExpRewardCommand implements BattleRewardCommand {

	private final int earnedExp;
	private int updatedExp;

	private int updatedLevel;
	private int levelUpAmount;

	public ExpRewardCommand(int earnedExp) {
		this.earnedExp = earnedExp;
	}

	@Override
	public void execute(GameCharacter ch) {
		int curExp = ch.getExp();
		int curLevel = ch.getLevel();

		updatedExp = Math.min(curExp + earnedExp, 9900);
		updatedLevel = updatedExp / 100 + 1;

		ch.updateExp(Math.min(ch.getExp() + earnedExp, 9900));
		ch.updateLevel(updatedLevel);

		this.levelUpAmount = updatedLevel - curLevel;
	}

}
