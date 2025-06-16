package com.example.rabbithell.domain.skill.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.rabbithell.domain.skill.dto.request.PassiveSkillCreateRequest;
import com.example.rabbithell.domain.skill.dto.request.PassiveSkillUpdateRequest;
import com.example.rabbithell.domain.skill.dto.response.AllPassiveSkillResponse;

public interface PassiveSkillService {
	Long createPassiveSkill(PassiveSkillCreateRequest request);

	Page<AllPassiveSkillResponse> getAllPassiveSkills(Pageable pageable, String jobName);

	void updatePassiveSkill(Long skillId, PassiveSkillUpdateRequest request);

	void deletePassiveSkill(Long skillId);
}
