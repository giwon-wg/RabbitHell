package com.example.rabbithell.domain.characterSkill.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.characterSkill.entity.CharacterSkill;

public interface CharacterSkillRepository extends JpaRepository<CharacterSkill,Long> {
}
