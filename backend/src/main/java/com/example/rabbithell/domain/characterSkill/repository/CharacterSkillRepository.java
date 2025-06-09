package com.example.rabbithell.domain.characterSkill.repository;

import static com.example.rabbithell.domain.characterSkill.exception.code.CharacterSkillExceptionCode.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.characterSkill.entity.CharacterSkill;
import com.example.rabbithell.domain.characterSkill.exception.CharacterSkillException;
import com.example.rabbithell.domain.skill.entity.Skill;

public interface CharacterSkillRepository extends JpaRepository<CharacterSkill,Long> {

	boolean existsByCharacterAndSkill(GameCharacter character, Skill skill);

	Optional<CharacterSkill> findByCharacterAndSkill(GameCharacter character, Skill skill);

	default CharacterSkill findByCharacterAndSkillOrElseThrow(GameCharacter character, Skill skill) {
		return findByCharacterAndSkill(character, skill)
			.orElseThrow(() -> new CharacterSkillException(NOT_LEARNED_YET));
	}

	Page<CharacterSkill> findByCharacter(GameCharacter character, Pageable pageable);

	List<Long> findSkillIdsByCharacter(GameCharacter character);

	List<CharacterSkill> findByCharacterAndEquippedTrue(GameCharacter character);
}
