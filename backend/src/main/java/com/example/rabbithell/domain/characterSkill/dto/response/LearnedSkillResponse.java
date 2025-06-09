package com.example.rabbithell.domain.characterSkill.dto.response;

import com.example.rabbithell.domain.characterSkill.entity.CharacterSkill;

public record LearnedSkillResponse(
	Long id,
	String name,
	int tier,
	int mpCost,
	int coolTime
) {
	public static LearnedSkillResponse from(CharacterSkill characterSkill) {
		return new LearnedSkillResponse(
			characterSkill.getSkill().getId(),
			characterSkill.getSkill().getName(),
			characterSkill.getSkill().getTier(),
			characterSkill.getSkill().getMpCost(),
			characterSkill.getSkill().getCoolTime()
		);
	}

}
