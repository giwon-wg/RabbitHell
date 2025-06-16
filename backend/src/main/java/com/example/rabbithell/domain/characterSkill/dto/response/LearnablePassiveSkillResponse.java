package com.example.rabbithell.domain.characterSkill.dto.response;

import com.example.rabbithell.domain.skill.entity.PassiveSkill;

public record LearnablePassiveSkillResponse(
	Long id,
	String name,
	int skillTier,
	String description
) {
	public static LearnablePassiveSkillResponse from(PassiveSkill passiveSkill) {
		return new LearnablePassiveSkillResponse(
			passiveSkill.getId(),
			passiveSkill.getName(),
			passiveSkill.getTier(),
			passiveSkill.getDescription()
		);
	}
}
