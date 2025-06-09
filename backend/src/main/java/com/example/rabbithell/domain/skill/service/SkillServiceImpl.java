package com.example.rabbithell.domain.skill.service;

import static com.example.rabbithell.domain.skill.exception.code.SkillExceptionCode.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.characterSkill.entity.CharacterSkill;
import com.example.rabbithell.domain.characterSkill.repository.CharacterSkillRepository;
import com.example.rabbithell.domain.skill.dto.request.SkillCreateRequest;
import com.example.rabbithell.domain.skill.dto.request.SkillUpdateRequest;
import com.example.rabbithell.domain.skill.dto.response.AllSkillResponse;
import com.example.rabbithell.domain.skill.entity.Skill;
import com.example.rabbithell.domain.skill.exception.SkillException;
import com.example.rabbithell.domain.skill.repository.SkillRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

	private final SkillRepository skillRepository;
	private final CharacterRepository characterRepository;
	private final CharacterSkillRepository characterSkillRepository;

	@Override
	public Long createSkill(SkillCreateRequest request) {

		Skill skill = Skill.builder()
			.name(request.name())
			.description(request.description())
			.requiredSkillPoint(request.requiredSkillPoint())
			.tier(request.tier())
			.mpCost(request.mpCost())
			.coolTime(request.coolTime())
			.dmg(request.dmg())
			.job(request.job())
			.skillType(request.skillType())
			.skillTarget(request.skillTarget())
			.build();

		skillRepository.save(skill);

		return skill.getId();
	}

	@Transactional(readOnly = true)
	@Override
	public Page<AllSkillResponse> getAllSkills(Pageable pageable, String jobName) {
		Page<Skill> skills;
		if (jobName == null || jobName.isEmpty()) {
			skills = skillRepository.findAll(pageable);
		} else {
			skills = skillRepository.findByJobNameIgnoreCase(jobName, pageable);
		}
		return skills.map(skill -> new AllSkillResponse(skill));
	}

	@Transactional
	@Override
	public void updateSkill(Long skillId, SkillUpdateRequest request) {

		Skill skill = skillRepository.findByIdOrElseThrow(skillId);

		skill.skillUpdate(
			request.name(),
			request.description(),
			request.requiredSkillPoint(),
			request.probability(),
			request.tier(),
			request.mpCost(),
			request.coolTime(),
			request.dmg(),
			request.job(),
			request.skillType(),
			request.skillTarget()
		);

		skillRepository.save(skill);
	}

	@Transactional
	@Override
	public void deleteSkill(Long skillId) {
		Skill skill = skillRepository.findByIdOrElseThrow(skillId);
		skillRepository.delete(skill);
	}
}
