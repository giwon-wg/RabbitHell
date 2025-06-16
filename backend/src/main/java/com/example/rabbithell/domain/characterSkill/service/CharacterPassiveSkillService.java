package com.example.rabbithell.domain.characterSkill.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.characterSkill.dto.response.EquippedPassiveSkillResponse;
import com.example.rabbithell.domain.characterSkill.dto.response.LearnablePassiveSkillResponse;
import com.example.rabbithell.domain.characterSkill.dto.response.LearnedPassiveSkillResponse;
import com.example.rabbithell.domain.characterSkill.enums.SkillEquipType;

public interface CharacterPassiveSkillService {

	void learnPassiveSkill(Long characterId, Long skillId);

	void equipPassiveSkill(Long characterId, Long skillId, SkillEquipType slotType);

	void unequipPassiveSkill(Long characterId, Long skillId);

	Page<LearnedPassiveSkillResponse> getLearnedPassiveSkills(Long characterId, Pageable pageable);

	Page<LearnablePassiveSkillResponse> getLearnablePassiveSkills(Long characterId, Pageable pageable);

	List<EquippedPassiveSkillResponse> getEquippedPassiveSkills(Long characterId);

	void unequipPassiveSkillsNotMatchingCurrentJob(GameCharacter character);

}
