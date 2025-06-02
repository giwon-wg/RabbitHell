package com.example.rabbithell.domain.util.learnSkillLogic;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.characterSkill.entity.CharacterSkill;
import com.example.rabbithell.domain.characterSkill.repository.CharacterSkillRepository;
import com.example.rabbithell.domain.job.entity.Job;
import com.example.rabbithell.domain.skill.entity.Skill;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LearnSkillLogic {

	public static void validateAndLearnSkill(
		GameCharacter character,
		Skill skill,
		CharacterSkillRepository characterSkillRepository
	) {
		Job characterJob = character.getJob();
		Job skillRequiredJob = skill.getJob();

		// 직업 카테고리 확인
		if (characterJob.getJobCategory() != skillRequiredJob.getJobCategory()) {
			throw new IllegalArgumentException("직업 계열이 달라 이 스킬을 배울 수 없습니다.");
		}

		// 티어는 같거나 높아야 함
		if (!characterJob.getTier().isSameOrHigherThan(skillRequiredJob.getTier())) {
			throw new IllegalArgumentException("현재 직업 티어로는 해당 스킬을 배울 수 없습니다.");
		}

		// 이미 배운 스킬은 배울 수 없음.
		if (characterSkillRepository.existsByCharacterAndSkill(character, skill)) {
			throw new IllegalArgumentException("이미 배운 스킬입니다.");
		}


		CharacterSkill characterSkill = new CharacterSkill(character, skill);
		characterSkillRepository.save(characterSkill);
	}

}
