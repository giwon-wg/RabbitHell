package com.example.rabbithell.domain.characterSkill.dto.response;

import com.example.rabbithell.domain.skill.entity.ActiveSkill;

public record LearnableActiveSkillResponse(
	Long id,
	String name,
	int skillTier,
	int mpCost,
	int coolTime
) {
	public static LearnableActiveSkillResponse from(ActiveSkill activeSkill) {
		return new LearnableActiveSkillResponse(
			activeSkill.getId(),
			activeSkill.getName(),
			activeSkill.getTier(),
			activeSkill.getMpCost(),
			activeSkill.getCoolTime()
		);
	}
}
