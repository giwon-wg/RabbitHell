package com.example.rabbithell.domain.skill.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.rabbithell.domain.skill.dto.request.ActiveSkillCreateRequest;
import com.example.rabbithell.domain.skill.dto.request.ActiveSkillUpdateRequest;
import com.example.rabbithell.domain.skill.dto.response.AllActiveSkillResponse;

public interface ActiveSkillService {

	Long createActiveSkill(ActiveSkillCreateRequest request);

	Page<AllActiveSkillResponse> getAllActiveSkills(Pageable pageable, String jobName);

	void updateActiveSkill(Long skillId, ActiveSkillUpdateRequest request);

	void deleteActiveSkill(Long skillId);

}
