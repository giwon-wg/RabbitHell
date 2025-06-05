package com.example.rabbithell.domain.battle.postProcess.command;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.job.entity.JobCategory;

import lombok.Getter;

public class JobPointRewardCommand implements BattleRewardCommand {

	private final GameCharacter character;
	private final int earnedSkillPoints;
	@Getter
	private int updatedJobPoints;

	public JobPointRewardCommand(GameCharacter character, int earnedSkillPoints) {
		this.character = character;
		this.earnedSkillPoints = earnedSkillPoints;
	}

	@Override
	public void execute() {
		JobCategory jobCategory = character.getJob().getJobCategory();
		if (jobCategory == JobCategory.INCOMPETENT) {
			this.updatedJobPoints = character.getIncompetentPoint() + earnedSkillPoints;
		} else if (jobCategory == JobCategory.WARRIOR) {
			this.updatedJobPoints = character.getWarriorPoint() + earnedSkillPoints;
		} else if (jobCategory == JobCategory.THIEF) {
			this.updatedJobPoints = character.getThiefPoint() + earnedSkillPoints;
		} else if (jobCategory == JobCategory.WIZARD) {
			this.updatedJobPoints = character.getWizardPoint() + earnedSkillPoints;
		} else if (jobCategory == JobCategory.ARCHER) {
			this.updatedJobPoints = character.getArcherPoint() + earnedSkillPoints;
		}
	}

}
