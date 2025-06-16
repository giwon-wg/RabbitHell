package com.example.rabbithell.domain.skill.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.skill.dto.request.ActiveSkillCreateRequest;
import com.example.rabbithell.domain.skill.dto.request.ActiveSkillUpdateRequest;
import com.example.rabbithell.domain.skill.dto.response.AllActiveSkillResponse;
import com.example.rabbithell.domain.skill.entity.ActiveSkill;
import com.example.rabbithell.domain.skill.repository.ActiveSkillRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActiveSkillServiceImpl implements ActiveSkillService {

	private final ActiveSkillRepository activeSkillRepository;

	@Override
	public Long createActiveSkill(ActiveSkillCreateRequest request) {

		ActiveSkill activeSkill = ActiveSkill.builder()
			.name(request.name())
			.description(request.description())
			.requiredSkillPoint(request.requiredSkillPoint())
			.tier(request.tier())
			.mpCost(request.mpCost())
			.coolTime(request.coolTime())
			.dmg(request.dmg())
			.job(request.job())
			.skillTarget(request.skillTarget())
			.skillType("ACTIVE")
			.build();

		activeSkillRepository.save(activeSkill);

		return activeSkill.getId();
	}

	@Transactional(readOnly = true)
	@Override
	public Page<AllActiveSkillResponse> getAllActiveSkills(Pageable pageable, String jobName) {
		Page<ActiveSkill> skills;
		if (jobName == null || jobName.isEmpty()) {
			skills = activeSkillRepository.findAll(pageable);
		} else {
			skills = activeSkillRepository.findByJobNameIgnoreCase(jobName, pageable);
		}
		return skills.map(skill -> new AllActiveSkillResponse(skill));
	}

	@Transactional
	@Override
	public void updateActiveSkill(Long skillId, ActiveSkillUpdateRequest request) {

		ActiveSkill activeSkill = activeSkillRepository.findByIdOrElseThrow(skillId);

		activeSkill.skillUpdate(
			request.name(),
			request.description(),
			request.requiredSkillPoint(),
			request.probability(),
			request.tier(),
			request.mpCost(),
			request.coolTime(),
			request.dmg(),
			request.job(),
			request.skillTarget()
		);

		activeSkillRepository.save(activeSkill);
	}

	@Transactional
	@Override
	public void deleteActiveSkill(Long skillId) {
		ActiveSkill activeSkill = activeSkillRepository.findByIdOrElseThrow(skillId);
		activeSkillRepository.delete(activeSkill);
	}

}
