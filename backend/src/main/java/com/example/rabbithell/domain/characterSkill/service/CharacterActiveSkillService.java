package com.example.rabbithell.domain.characterSkill.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.characterSkill.dto.response.EquippedActiveSkillResponse;
import com.example.rabbithell.domain.characterSkill.dto.response.LearnableActiveSkillResponse;
import com.example.rabbithell.domain.characterSkill.dto.response.LearnedActiveSkillResponse;
import com.example.rabbithell.domain.characterSkill.enums.SkillEquipType;

public interface CharacterActiveSkillService {

	void learnActiveSkill(Long characterId, Long skillId);

	void equipActiveSkill(Long characterId, Long skillId, SkillEquipType slotType);

	void unequipActiveSkill(Long characterId, Long skillId);

	Page<LearnedActiveSkillResponse> getLearnedActiveSkills(Long characterId, Pageable pageable);

	Page<LearnableActiveSkillResponse> getLearnableActiveSkills(Long characterId, Pageable pageable);

	List<EquippedActiveSkillResponse> getEquippedActiveSkills(Long characterId);

	void unequipActiveSkillsNotMatchingCurrentJob(GameCharacter character);

}
