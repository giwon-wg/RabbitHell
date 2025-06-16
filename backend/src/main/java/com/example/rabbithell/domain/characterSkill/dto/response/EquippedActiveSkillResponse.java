package com.example.rabbithell.domain.characterSkill.dto.response;

import com.example.rabbithell.domain.characterSkill.entity.CharacterActiveSkill;
import com.example.rabbithell.domain.characterSkill.enums.SkillEquipType;

public record EquippedActiveSkillResponse(
	Long id,
	String name,
	SkillEquipType slotType
) {
	public static EquippedActiveSkillResponse from(CharacterActiveSkill characterActiveSkill) {
		return new EquippedActiveSkillResponse(
			characterActiveSkill.getId(),
			characterActiveSkill.getActiveSkill().getName(),
			characterActiveSkill.getEquipType()
		);
	}
}
