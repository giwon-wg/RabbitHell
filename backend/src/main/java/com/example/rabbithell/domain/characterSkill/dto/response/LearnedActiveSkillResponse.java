package com.example.rabbithell.domain.characterSkill.dto.response;

import com.example.rabbithell.domain.characterSkill.entity.CharacterActiveSkill;
import com.example.rabbithell.domain.characterSkill.enums.SkillEquipType;

public record LearnedActiveSkillResponse(
	Long id,
	String name,
	int tier,
	int mpCost,
	int coolTime,
	SkillEquipType equipType
) {
	public static LearnedActiveSkillResponse from(CharacterActiveSkill characterActiveSkill) {
		return new LearnedActiveSkillResponse(
			characterActiveSkill.getActiveSkill().getId(),
			characterActiveSkill.getActiveSkill().getName(),
			characterActiveSkill.getActiveSkill().getTier(),
			characterActiveSkill.getActiveSkill().getMpCost(),
			characterActiveSkill.getActiveSkill().getCoolTime(),
			characterActiveSkill.getEquipType()
		);
	}

}
