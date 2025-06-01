package com.example.rabbithell.domain.skill.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.rabbithell.domain.job.entity.Job;
import com.example.rabbithell.domain.skill.dto.request.SkillCreateRequest;
import com.example.rabbithell.domain.skill.dto.response.AllSkillResponse;
import com.example.rabbithell.domain.skill.entity.Skill;
import com.example.rabbithell.domain.skill.repository.SkillRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService{

	private final SkillRepository skillRepository;

	@Override
	public Long createSkill(SkillCreateRequest request) {

		Job job = Job.fromName(request.jobName());

		Skill skill = Skill.builder()
			.name(request.name())
			.description(request.description())
			.tier(request.tier())
			.mpCost(request.mpCost())
			.coolTime(request.coolTime())
			.dmg(request.dmg())
			.job(job)
			.build();

		skillRepository.save(skill);

		return skill.getId();
	}

	@Override
	public Page<AllSkillResponse> getAllSkills(Pageable pageable,String jobName) {
		Page<Skill> skills;
		if (jobName == null || jobName.isEmpty()) {
			skills = skillRepository.findAll(pageable);
		} else {
			skills = skillRepository.findByJobNameIgnoreCase(jobName, pageable);
		}
		return skills.map(skill -> new AllSkillResponse(skill));
	}
}
