package com.example.rabbithell.domain.battle.postProcess.command;

import com.example.rabbithell.domain.character.entity.GameCharacter;

import lombok.Getter;

@Getter
public class SkillPointRewardCommand implements BattleRewardCommand {

	private final int earnedSkillPoints;
	private int updatedSkillPoints;

	public SkillPointRewardCommand(int earnedSkillPoints) {
		this.earnedSkillPoints = earnedSkillPoints;
	}

	@Override
	public void execute(GameCharacter ch) {
		updatedSkillPoints = ch.getSkillPoint() + earnedSkillPoints;
		ch.updateSkillPoint(updatedSkillPoints);
	}

}
