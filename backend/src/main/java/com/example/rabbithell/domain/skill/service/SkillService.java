package com.example.rabbithell.domain.skill.service;

import com.example.rabbithell.domain.skill.dto.request.SkillCreateRequest;

public interface SkillService {

	Long createSkill(SkillCreateRequest request);

}
