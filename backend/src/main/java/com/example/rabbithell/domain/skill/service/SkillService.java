package com.example.rabbithell.domain.skill.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.rabbithell.domain.skill.dto.request.SkillCreateRequest;
import com.example.rabbithell.domain.skill.dto.response.AllSkillResponse;

public interface SkillService {

	Long createSkill(SkillCreateRequest request);

	Page<AllSkillResponse> getAllSkills(Pageable pageable,String jobName);

}
