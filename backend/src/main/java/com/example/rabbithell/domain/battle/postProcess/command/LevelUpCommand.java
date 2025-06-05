package com.example.rabbithell.domain.battle.postProcess.command;

import com.example.rabbithell.domain.character.entity.GameCharacter;

import lombok.Getter;

public class LevelUpCommand implements BattleRewardCommand {

	private final GameCharacter character;
	private final int baseExp;
	@Getter
	private int resultLevel;
	@Getter
	private int levelUpAmount;

	public LevelUpCommand(GameCharacter character, int baseExp) {
		this.character = character;
		this.baseExp = baseExp;
	}

	@Override
	public void execute() {
		resultLevel = baseExp / 100 + 1;
		levelUpAmount = Math.max(resultLevel - character.getLevel(), 0);
	}

}
