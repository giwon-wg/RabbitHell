package com.example.rabbithell.domain.characterSkill.dto.response;

import com.example.rabbithell.domain.characterSkill.entity.CharacterSkill;

public record EquippedSkillResponse(
	Long id,
	String name,
	int slotIndex
) {
	public static EquippedSkillResponse from(CharacterSkill characterSkill, int slotIndex) {
		return new EquippedSkillResponse(
			characterSkill.getId(),
			characterSkill.getSkill().getName(),
			slotIndex
		);
	}
}
