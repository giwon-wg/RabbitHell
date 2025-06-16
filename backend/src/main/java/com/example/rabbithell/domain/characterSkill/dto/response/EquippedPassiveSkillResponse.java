package com.example.rabbithell.domain.characterSkill.dto.response;

import com.example.rabbithell.domain.characterSkill.entity.CharacterPassiveSkill;
import com.example.rabbithell.domain.characterSkill.enums.SkillEquipType;

public record EquippedPassiveSkillResponse(
	Long id,
	String name,
	SkillEquipType slotType
) {
	public static EquippedPassiveSkillResponse from(CharacterPassiveSkill characterPassiveSkill) {
		return new EquippedPassiveSkillResponse(
			characterPassiveSkill.getId(),
			characterPassiveSkill.getPassiveSkill().getName(),
			characterPassiveSkill.getEquipType()
		);
	}
}
