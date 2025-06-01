package com.example.rabbithell.domain.skill.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.skill.entity.Skill;

public interface SkillRepository extends JpaRepository<Skill,Long> {
	Page<Skill> findByJobNameIgnoreCase(String jobName, Pageable pageable);
}
