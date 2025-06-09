package com.example.rabbithell.domain.battle.postProcess.command;

import com.example.rabbithell.domain.character.entity.GameCharacter;

import lombok.Getter;

@Getter
public class JobPointRewardCommand implements BattleRewardCommand {

	private final int earnedSkillPoints;

	public JobPointRewardCommand(int earnedSkillPoints) {
		this.earnedSkillPoints = earnedSkillPoints;
	}

	@Override
	public void execute(GameCharacter ch) {
		ch.updateJobPoint(earnedSkillPoints);
	}
}
