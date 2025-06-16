package com.example.rabbithell.domain.characterSkill.dto.response;

import com.example.rabbithell.domain.characterSkill.entity.CharacterPassiveSkill;
import com.example.rabbithell.domain.characterSkill.enums.SkillEquipType;

public record LearnedPassiveSkillResponse(
	Long id,
	String name,
	int skillTier,
	SkillEquipType skillEquipType
) {
	public static LearnedPassiveSkillResponse from(CharacterPassiveSkill characterPassiveSkill) {
		return new LearnedPassiveSkillResponse(
			characterPassiveSkill.getPassiveSkill().getId(),
			characterPassiveSkill.getPassiveSkill().getName(),
			characterPassiveSkill.getPassiveSkill().getTier(),
			characterPassiveSkill.getEquipType()
		);
	}
}
