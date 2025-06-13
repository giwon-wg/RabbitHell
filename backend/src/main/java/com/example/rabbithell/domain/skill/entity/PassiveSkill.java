package com.example.rabbithell.domain.skill.entity;

import com.example.rabbithell.domain.job.entity.Job;
import com.example.rabbithell.domain.job.entity.JobCategory;
import com.example.rabbithell.domain.skill.enums.PassiveType;
import com.example.rabbithell.domain.skill.enums.SkillTarget;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class PassiveSkill {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String description;

	private int requiredSkillPoint;

	private int value;

	private int tier;

	@Enumerated(EnumType.STRING)
	private PassiveType passiveType;

	@Enumerated(EnumType.STRING)
	private Job job;

	@Enumerated(EnumType.STRING)
	private JobCategory jobCategory;

	private int jobTier;

	private SkillTarget skillTarget;

	private String skillType = "PASSIVE";

	@Builder
	public PassiveSkill(
		String name,
		String description,
		int requiredSkillPoint,
		int value,
		int tier,
		PassiveType passiveType,
		Job job,
		SkillTarget skillTarget,
		String skillType
	) {
		this.name = name;
		this.description = description;
		this.requiredSkillPoint = requiredSkillPoint;
		this.value = value;
		this.tier = tier;
		this.passiveType = passiveType;
		this.job = job;
		this.jobCategory = job.getJobCategory();
		this.jobTier = job.getTier().getTier();
		this.skillType = skillType;
		this.skillTarget = skillTarget;
	}

	public void passiveSkillUpdate(
		String name,
		String description,
		int requiredSkillPoint,
		int value,
		int tier,
		PassiveType passiveType,
		Job job,
		SkillTarget skillTarget
	) {
		this.name = name;
		this.description = description;
		this.requiredSkillPoint = requiredSkillPoint;
		this.value = value;
		this.tier = tier;
		this.passiveType = passiveType;
		this.job = job;
		this.jobCategory = job.getJobCategory();
		this.jobTier = job.getTier().getTier();
		this.skillTarget = skillTarget;
	}
}
