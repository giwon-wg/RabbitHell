package com.example.rabbithell.domain.skill.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.skill.dto.request.PassiveSkillCreateRequest;
import com.example.rabbithell.domain.skill.dto.request.PassiveSkillUpdateRequest;
import com.example.rabbithell.domain.skill.dto.response.AllPassiveSkillResponse;
import com.example.rabbithell.domain.skill.entity.PassiveSkill;
import com.example.rabbithell.domain.skill.repository.PassiveSkillRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PassiveSkillServiceImpl implements PassiveSkillService{

	private final PassiveSkillRepository passiveSkillRepository;

	@Override
	public Long createPassiveSkill(PassiveSkillCreateRequest request) {

		PassiveSkill passiveSkill = PassiveSkill.builder()
			.name(request.name())
			.description(request.description())
			.requiredSkillPoint(request.requiredSkillPoint())
			.passiveType(request.passiveType())
			.tier(request.tier())
			.job(request.job())
			.skillTarget(request.skillTarget())
			.skillType("PASSIVE")
			.build();

		passiveSkillRepository.save(passiveSkill);

		return passiveSkill.getId();
	}

	@Transactional(readOnly = true)
	@Override
	public Page<AllPassiveSkillResponse> getAllPassiveSkills(Pageable pageable, String jobName) {
		Page<PassiveSkill> skills;
		if (jobName == null || jobName.isEmpty()) {
			skills = passiveSkillRepository.findAll(pageable);
		} else {
			skills = passiveSkillRepository.findByJobNameIgnoreCase(jobName, pageable);
		}
		return skills.map(skill -> new AllPassiveSkillResponse(skill));
	}

	@Transactional
	@Override
	public void updatePassiveSkill(Long skillId, PassiveSkillUpdateRequest request) {

		PassiveSkill passiveSkill = passiveSkillRepository.findByIdOrElseThrow(skillId);

		passiveSkill.passiveSkillUpdate(
			request.name(),
			request.description(),
			request.requiredSkillPoint(),
			request.value(),
			request.tier(),
			request.passiveType(),
			request.job(),
			request.skillTarget()
		);

		passiveSkillRepository.save(passiveSkill);
	}

	@Transactional
	@Override
	public void deletePassiveSkill(Long skillId) {
		PassiveSkill passiveSkill = passiveSkillRepository.findByIdOrElseThrow(skillId);
		passiveSkillRepository.delete(passiveSkill);
	}
}
