package com.example.rabbithell.domain.skill.repository;

import static com.example.rabbithell.domain.character.exception.code.CharacterExceptionCode.*;
import static com.example.rabbithell.domain.skill.exception.code.SkillExceptionCode.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.character.exception.CharacterException;
import com.example.rabbithell.domain.skill.entity.Skill;
import com.example.rabbithell.domain.skill.exception.SkillException;
import com.example.rabbithell.domain.skill.exception.code.SkillExceptionCode;

public interface SkillRepository extends JpaRepository<Skill, Long> {
	Page<Skill> findByJobNameIgnoreCase(String jobName, Pageable pageable);

	default Skill findByIdOrElseThrow(Long id) {
		return findById(id)
			.orElseThrow(() -> new SkillException(SKILL_NOT_FOUND));
	}
}
