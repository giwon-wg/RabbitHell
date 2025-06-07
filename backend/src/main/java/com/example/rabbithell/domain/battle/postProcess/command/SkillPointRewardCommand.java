package com.example.rabbithell.domain.battle.postProcess.command;

import com.example.rabbithell.domain.character.entity.GameCharacter;

import lombok.Getter;

@Getter
public class SkillPointRewardCommand implements BattleRewardCommand {

	private final GameCharacter character;
	private final int earnedSkillPoints;
	private int updatedSkillPoints;

	public SkillPointRewardCommand(GameCharacter character, int earnedSkillPoints) {
		this.character = character;
		this.earnedSkillPoints = earnedSkillPoints;
	}

	@Override
	public void execute() {
		this.updatedSkillPoints = character.getSkillPoint() + earnedSkillPoints;
	}

}
