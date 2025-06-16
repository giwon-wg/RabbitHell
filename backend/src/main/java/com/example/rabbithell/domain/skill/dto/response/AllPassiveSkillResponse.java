package com.example.rabbithell.domain.skill.dto.response;

import com.example.rabbithell.domain.job.entity.Job;
import com.example.rabbithell.domain.job.entity.JobCategory;
import com.example.rabbithell.domain.skill.entity.ActiveSkill;
import com.example.rabbithell.domain.skill.entity.PassiveSkill;
import com.example.rabbithell.domain.skill.enums.PassiveType;
import com.example.rabbithell.domain.skill.enums.SkillTarget;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record AllPassiveSkillResponse(
	Long id,
	String name,
	String description,
	int requiredSkillPoint,
	int value,
	int tier ,
	PassiveType passiveTyp,
	Job job,
	JobCategory jobCategory,
	int jobTier,
	SkillTarget skillTarget,
	String skillType
) {
	public AllPassiveSkillResponse(PassiveSkill passiveSkill) {
		this(
			passiveSkill.getId(),
			passiveSkill.getName(),
			passiveSkill.getDescription(),
			passiveSkill.getRequiredSkillPoint(),
			passiveSkill.getValue(),
			passiveSkill.getTier(),
			passiveSkill.getPassiveType(),
			passiveSkill.getJob(),
			passiveSkill.getJob().getJobCategory(),
			passiveSkill.getJob().getTier().getTier(),
			passiveSkill.getSkillTarget(),
			passiveSkill.getSkillType()
		);
	}
}
