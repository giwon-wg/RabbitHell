package com.example.rabbithell.domain.skill.dto.response;

import com.example.rabbithell.domain.skill.entity.Skill;

public record AllSkillResponse(
	Long id,
	String name,
	String description,
	int tier,
	int mpCost,
	int coolTime,
	int dmg,
	String jobName
) {
	public AllSkillResponse(Skill skill) {
		this(
			skill.getId(),
			skill.getName(),
			skill.getDescription(),
			skill.getTier(),
			skill.getMpCost(),
			skill.getCoolTime(),
			skill.getDmg(),
			skill.getJob().getName()
		);
	}
}
