package com.example.rabbithell.domain.skill.dto.response;

import com.example.rabbithell.domain.skill.entity.ActiveSkill;

public record AllActiveSkillResponse(
	Long id,
	String name,
	String description,
	int tier,
	int mpCost,
	int coolTime,
	int dmg,
	String jobName
) {
	public AllActiveSkillResponse(ActiveSkill activeSkill) {
		this(
			activeSkill.getId(),
			activeSkill.getName(),
			activeSkill.getDescription(),
			activeSkill.getTier(),
			activeSkill.getMpCost(),
			activeSkill.getCoolTime(),
			activeSkill.getDmg(),
			activeSkill.getJob().getName()
		);
	}
}
