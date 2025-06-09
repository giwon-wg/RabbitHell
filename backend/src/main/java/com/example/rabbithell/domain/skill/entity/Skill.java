package com.example.rabbithell.domain.skill.entity;

import com.example.rabbithell.domain.job.entity.Job;
import com.example.rabbithell.domain.job.entity.JobCategory;
import com.example.rabbithell.domain.skill.enums.SkillTarget;
import com.example.rabbithell.domain.skill.enums.SkillType;

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
public class Skill {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String description;

	private int requiredSkillPoint;

	private int probability;

	private int tier;

	private int mpCost;

	private int coolTime;

	private int dmg;

	// 직업 카테고리로 변경.
	@Enumerated(EnumType.STRING)
	private Job job;

	@Enumerated(EnumType.STRING)
	private JobCategory jobCategory;

	private int jobTier;

	private SkillType skillType;

	private SkillTarget skillTarget;

	@Builder
	public Skill(
		String name,
		String description,
		int requiredSkillPoint,
		int probability,
		int tier,
		int mpCost,
		int coolTime,
		int dmg,
		Job job,
        SkillType skillType,
		SkillTarget skillTarget
	) {
		this.name = name;
		this.description = description;
		this.requiredSkillPoint = requiredSkillPoint;
		this.probability = probability;
		this.tier = tier;
		this.mpCost = mpCost;
		this.coolTime = coolTime;
		this.dmg = dmg;
		this.job = job;
		this.jobCategory = job.getJobCategory();
		this.jobTier = job.getTier().getTier();
		this.skillType = skillType;
		this.skillTarget = skillTarget;
	}

	public void skillUpdate(
		String name,
		String description,
		int requiredSkillPoint,
		int probability,
		int tier,
		int mpCost,
		int coolTime,
		int dmg,
		Job job,
		SkillType skillType,
		SkillTarget skillTarget
	) {
		this.name = name;
		this.description = description;
		this.requiredSkillPoint = requiredSkillPoint;
		this.probability = probability;
		this.tier = tier;
		this.mpCost = mpCost;
		this.coolTime = coolTime;
		this.dmg = dmg;
		this.job = job;
		this.jobCategory = job.getJobCategory();
		this.jobTier = job.getTier().getTier();
		this.skillType = skillType;
		this.skillTarget = skillTarget;
	}

}
