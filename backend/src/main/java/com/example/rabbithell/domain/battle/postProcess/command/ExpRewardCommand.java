package com.example.rabbithell.domain.battle.postProcess.command;

import com.example.rabbithell.domain.character.entity.GameCharacter;

import lombok.Getter;

public class ExpRewardCommand implements BattleRewardCommand {

	private final GameCharacter character;
	private final int earnedExp;
	@Getter
	private int resultExp;

	public ExpRewardCommand(GameCharacter character, int earnedExp) {
		this.character = character;
		this.earnedExp = earnedExp;
	}

	@Override
	public void execute() {
		resultExp = Math.min(character.getExp() + earnedExp, 9900);
	}

}
