package com.example.rabbithell.domain.characterSkill.dto.response;

import com.example.rabbithell.domain.characterSkill.entity.CharacterSkill;
import com.example.rabbithell.domain.characterSkill.enums.SkillEquipType;

public record EquippedSkillResponse(
	Long id,
	String name,
	SkillEquipType slotType
) {
	public static EquippedSkillResponse from(CharacterSkill characterSkill) {
		return new EquippedSkillResponse(
			characterSkill.getId(),
			characterSkill.getSkill().getName(),
			characterSkill.getEquipType()
		);
	}
}
