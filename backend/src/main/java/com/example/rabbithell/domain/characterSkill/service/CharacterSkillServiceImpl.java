package com.example.rabbithell.domain.characterSkill.service;

import static com.example.rabbithell.domain.characterSkill.exception.code.CharacterSkillExceptionCode.*;
import static com.example.rabbithell.domain.skill.exception.code.SkillExceptionCode.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.characterSkill.dto.response.EquippedSkillResponse;
import com.example.rabbithell.domain.characterSkill.dto.response.LearnedSkillResponse;
import com.example.rabbithell.domain.characterSkill.entity.CharacterSkill;
import com.example.rabbithell.domain.characterSkill.exception.CharacterSkillException;
import com.example.rabbithell.domain.characterSkill.repository.CharacterSkillRepository;
import com.example.rabbithell.domain.skill.entity.Skill;
import com.example.rabbithell.domain.skill.exception.SkillException;
import com.example.rabbithell.domain.skill.repository.SkillRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CharacterSkillServiceImpl implements CharacterSkillService {

	private final CharacterRepository characterRepository;
	private final CharacterSkillRepository characterSkillRepository;
	private final SkillRepository skillRepository;

	@Override
	@Transactional
	public void learnSkill(Long characterId, Long skillId) {
		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);
		Skill skill = skillRepository.findByIdOrElseThrow(skillId);

		if (characterSkillRepository.existsByCharacterAndSkill(character, skill)) {
			throw new SkillException(ALREADY_LEARNED);
		}

		if (character.getJob().getJobCategory() != skill.getJob().getJobCategory()) {
			throw new SkillException(INVALID_JOB_FOR_SKILL);
		}

		if (!character.getJob().getTier().isSameOrHigherThan(skill.getJob().getTier())) {
			throw new SkillException(INSUFFICIENT_TIER_FOR_SKILL);
		}

		character.learnSkillBySkillPoint(skill.getRequiredSkillPoint());
		characterRepository.save(character);

		CharacterSkill characterSkill = new CharacterSkill(character, skill);
		characterSkillRepository.save(characterSkill);
	}

	@Override
	@Transactional
	public void equipSkill(Long characterId, Long skillId) {
		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);
		Skill skill = skillRepository.findByIdOrElseThrow(skillId);

		CharacterSkill characterSkill = characterSkillRepository.findByCharacterAndSkillOrElseThrow(character, skill);

		if (characterSkill.isEquipped()) {
			throw new CharacterSkillException(ALREADY_EQUIPPED);
		}

		characterSkill.equip();
	}

	@Override
	@Transactional
	public void unequipSkill(Long characterId, Long skillId) {
		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);
		Skill skill = skillRepository.findByIdOrElseThrow(skillId);

		CharacterSkill characterSkill = characterSkillRepository.findByCharacterAndSkillOrElseThrow(character, skill);

		if (!characterSkill.isEquipped()) {
			throw new CharacterSkillException(ALREADY_UNEQUIPPED);
		}

		characterSkill.unequip();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<LearnedSkillResponse> getLearnedSkills(Long characterId, Pageable pageable) {
		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);

		Page<CharacterSkill> learnedSkills = characterSkillRepository.findByCharacter(character, pageable);

		return learnedSkills.map(LearnedSkillResponse::from);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<LearnedSkillResponse> getLearnableSkills(Long characterId, Pageable pageable) {
		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);

		List<Long> learnedSkillIds = characterSkillRepository.findSkillIdsByCharacter(character);

		Page<Skill> skills = skillRepository.findLearnableSkills(
			character.getJob().getJobCategory(),
			character.getJob().getTier().getTier(),
			learnedSkillIds,
			pageable
		);

		return skills.map(skill -> new LearnedSkillResponse(
				skill.getId(),
				skill.getName(),
				skill.getTier(),
				skill.getMpCost(),
				skill.getCoolTime()
			)
		);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EquippedSkillResponse> getEquippedSkills(Long characterId) {
		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);

		List<CharacterSkill> equippedSkills = characterSkillRepository.findByCharacterAndEquippedTrue(character);

		return equippedSkills.stream()
			.map(cs -> EquippedSkillResponse.from(cs, equippedSkills.indexOf(cs)))
			.collect(Collectors.toList());
	}
}
