package com.example.rabbithell.domain.characterSkill.repository;

import static com.example.rabbithell.domain.characterSkill.exception.code.CharacterSkillExceptionCode.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.characterSkill.entity.CharacterPassiveSkill;
import com.example.rabbithell.domain.characterSkill.enums.SkillEquipType;
import com.example.rabbithell.domain.characterSkill.exception.CharacterSkillException;
import com.example.rabbithell.domain.skill.entity.PassiveSkill;

public interface CharacterPassiveSkillRepository extends JpaRepository<CharacterPassiveSkill, Long> {

	boolean existsByCharacterAndPassiveSkill(GameCharacter character, PassiveSkill passiveSkill);

	Optional<CharacterPassiveSkill> findByCharacterAndPassiveSkill(GameCharacter character, PassiveSkill passiveSkill);

	default CharacterPassiveSkill findByCharacterAndPassiveSkillOrElseThrow(GameCharacter character,
		PassiveSkill passiveSkill) {
		return findByCharacterAndPassiveSkill(character, passiveSkill)
			.orElseThrow(() -> new CharacterSkillException(NOT_LEARNED_YET));
	}

	Page<CharacterPassiveSkill> findByCharacter(GameCharacter character, Pageable pageable);

	List<Long> findSkillIdsByCharacter(GameCharacter character);

	Optional<CharacterPassiveSkill> findByCharacterAndEquipType(GameCharacter character, SkillEquipType slotType);

	@Query("select cps from CharacterPassiveSkill cps where cps.character = :character and cps.equipType <> com.example.rabbithell.domain.characterSkill.enums.SkillEquipType.NONE")
	List<CharacterPassiveSkill> findByCharacterAndEquippedTrue(GameCharacter character);
}
