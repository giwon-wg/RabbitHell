package com.example.rabbithell.domain.characterSkill.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.characterSkill.entity.CharacterSkill;
import com.example.rabbithell.domain.skill.entity.Skill;

public interface CharacterSkillRepository extends JpaRepository<CharacterSkill,Long> {

	boolean existsByCharacterAndSkill(GameCharacter character, Skill skill);

}
