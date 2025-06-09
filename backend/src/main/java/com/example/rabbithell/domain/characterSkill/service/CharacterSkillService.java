package com.example.rabbithell.domain.characterSkill.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.rabbithell.domain.characterSkill.dto.response.EquippedSkillResponse;
import com.example.rabbithell.domain.characterSkill.dto.response.LearnedSkillResponse;

public interface CharacterSkillService {

	void learnSkill(Long characterId, Long skillId);

	void equipSkill(Long characterId, Long skillId);

	void unequipSkill(Long characterId, Long skillId);

	Page<LearnedSkillResponse> getLearnedSkills(Long characterId, Pageable pageable);

	Page<LearnedSkillResponse> getLearnableSkills(Long characterId, Pageable pageable);

	List<EquippedSkillResponse> getEquippedSkills(Long characterId);

}
