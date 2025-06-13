package com.example.rabbithell.domain.skill.repository;

import static com.example.rabbithell.domain.skill.exception.code.SkillExceptionCode.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.rabbithell.domain.job.entity.JobCategory;
import com.example.rabbithell.domain.skill.entity.ActiveSkill;
import com.example.rabbithell.domain.skill.entity.PassiveSkill;
import com.example.rabbithell.domain.skill.exception.SkillException;

public interface PassiveSkillRepository extends JpaRepository<PassiveSkill, Long> {

	Page<PassiveSkill> findByJobNameIgnoreCase(String jobName, Pageable pageable);

	default PassiveSkill findByIdOrElseThrow(Long id) {
		return findById(id)
			.orElseThrow(() -> new SkillException(SKILL_NOT_FOUND));
	}

	@Query("SELECT s FROM PassiveSkill s " +
		"WHERE s.jobCategory = :jobCategory " +
		"AND s.jobTier <= :tier " +
		"AND (COALESCE(:learnedSkillIds, NULL) IS NULL OR s.id NOT IN :learnedSkillIds)")
	Page<PassiveSkill> findLearnableSkills(
		@Param("jobCategory") JobCategory jobCategory,
		@Param("tier") int tier,
		@Param("learnedSkillIds") List<Long> learnedSkillIds,
		Pageable pageable
	);

}
