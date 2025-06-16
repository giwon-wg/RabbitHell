package com.example.rabbithell.domain.characterSkill.repository;

import static com.example.rabbithell.domain.characterSkill.exception.code.CharacterSkillExceptionCode.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.characterSkill.entity.CharacterActiveSkill;
import com.example.rabbithell.domain.characterSkill.entity.CharacterPassiveSkill;
import com.example.rabbithell.domain.characterSkill.enums.SkillEquipType;
import com.example.rabbithell.domain.characterSkill.exception.CharacterSkillException;
import com.example.rabbithell.domain.skill.entity.ActiveSkill;

public interface CharacterActiveSkillRepository extends JpaRepository<CharacterActiveSkill, Long> {

	boolean existsByCharacterAndActiveSkill(GameCharacter character, ActiveSkill activeSkill);

	Optional<CharacterActiveSkill> findByCharacterAndActiveSkill(GameCharacter character, ActiveSkill activeSkill);

	default CharacterActiveSkill findByCharacterAndActiveSkillOrElseThrow(GameCharacter character,
		ActiveSkill activeSkill) {
		return findByCharacterAndActiveSkill(character, activeSkill)
			.orElseThrow(() -> new CharacterSkillException(NOT_LEARNED_YET));
	}

	Page<CharacterActiveSkill> findByCharacter(GameCharacter character, Pageable pageable);

	List<CharacterActiveSkill> findByCharacter(GameCharacter character);

	List<Long> findSkillIdsByCharacter(GameCharacter character);

	@Query("select cas from CharacterActiveSkill cas where cas.character = :character and cas.equipType = :slotType")
	Optional<CharacterActiveSkill> findByCharacterAndEquipType(GameCharacter character, SkillEquipType slotType);

}
