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
import com.example.rabbithell.domain.characterSkill.dto.response.EquippedPassiveSkillResponse;
import com.example.rabbithell.domain.characterSkill.dto.response.LearnablePassiveSkillResponse;
import com.example.rabbithell.domain.characterSkill.dto.response.LearnedPassiveSkillResponse;
import com.example.rabbithell.domain.characterSkill.entity.CharacterPassiveSkill;
import com.example.rabbithell.domain.characterSkill.enums.SkillEquipType;
import com.example.rabbithell.domain.characterSkill.exception.CharacterSkillException;
import com.example.rabbithell.domain.characterSkill.repository.CharacterPassiveSkillRepository;
import com.example.rabbithell.domain.skill.entity.PassiveSkill;
import com.example.rabbithell.domain.skill.exception.SkillException;
import com.example.rabbithell.domain.skill.repository.PassiveSkillRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CharacterPassiveSkillServiceImpl implements CharacterPassiveSkillService {

	private final CharacterRepository characterRepository;
	private final CharacterPassiveSkillRepository characterPassiveSkillRepository;
	private final PassiveSkillRepository passiveSkillRepository;

	@Override
	@Transactional
	public void learnPassiveSkill(Long characterId, Long skillId) {
		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);
		PassiveSkill passiveSkill = passiveSkillRepository.findByIdOrElseThrow(skillId);

		if (characterPassiveSkillRepository.existsByCharacterAndPassiveSkill(character, passiveSkill)) {
			throw new SkillException(ALREADY_LEARNED);
		}

		if (character.getJob().getJobCategory() != passiveSkill.getJob().getJobCategory()) {
			throw new SkillException(INVALID_JOB_FOR_SKILL);
		}

		if (!character.getJob().getTier().isSameOrHigherThan(passiveSkill.getJob().getTier())) {
			throw new SkillException(INSUFFICIENT_TIER_FOR_SKILL);
		}

		character.learnSkillBySkillPoint(passiveSkill.getRequiredSkillPoint());
		characterRepository.save(character);

		characterPassiveSkillRepository.save(new CharacterPassiveSkill(character, passiveSkill));
	}

	@Override
	@Transactional
	public void equipPassiveSkill(Long characterId, Long skillId, SkillEquipType slotType) {

		if (slotType != SkillEquipType.PASSIVE_SLOT) {
			throw new CharacterSkillException(INVALID_EQUIP_TYPE);
		}

		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);
		PassiveSkill passiveSkill = passiveSkillRepository.findByIdOrElseThrow(skillId);

		// 스킬 학습x 인경우
		CharacterPassiveSkill characterPassiveSkill = characterPassiveSkillRepository
			.findByCharacterAndPassiveSkillOrElseThrow(character, passiveSkill);

		// 직업 체크
		if (!character.getJob().getJobCategory().equals(passiveSkill.getJob().getJobCategory())) {
			throw new SkillException(INVALID_JOB_FOR_SKILL);
		}

		// 티어 체크
		if (!character.getJob().getTier().isSameOrHigherThan(passiveSkill.getJob().getTier())) {
			throw new SkillException(INSUFFICIENT_TIER_FOR_SKILL);
		}

		// 이미 패시브 슬롯에 장착된 스킬이 있으면 해제
		characterPassiveSkillRepository.findByCharacterAndEquipType(character, SkillEquipType.PASSIVE_SLOT)
			.ifPresent(CharacterPassiveSkill::unequip);

		characterPassiveSkill.equip(slotType);
	}

	@Override
	@Transactional
	public void unequipPassiveSkill(Long characterId, Long skillId) {
		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);
		PassiveSkill passiveSkill = passiveSkillRepository.findByIdOrElseThrow(skillId);

		CharacterPassiveSkill characterPassiveSkill = characterPassiveSkillRepository.findByCharacterAndPassiveSkillOrElseThrow(
			character, passiveSkill);

		if (!characterPassiveSkill.isEquipped()) {
			throw new CharacterSkillException(ALREADY_UNEQUIPPED);
		}

		characterPassiveSkill.unequip();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<LearnedPassiveSkillResponse> getLearnedPassiveSkills(Long characterId, Pageable pageable) {
		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);

		return characterPassiveSkillRepository.findByCharacter(character, pageable)
			.map(LearnedPassiveSkillResponse::from);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<LearnablePassiveSkillResponse> getLearnablePassiveSkills(Long characterId, Pageable pageable) {
		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);
		List<Long> learnedSkillIds = characterPassiveSkillRepository.findSkillIdsByCharacter(character);

		return passiveSkillRepository.findLearnableSkills(
			character.getJob().getJobCategory(),
			character.getJob().getTier().getTier(),
			learnedSkillIds,
			pageable
		).map(LearnablePassiveSkillResponse::from);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EquippedPassiveSkillResponse> getEquippedPassiveSkills(Long characterId) {
		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);

		List<CharacterPassiveSkill> equippedSkills = characterPassiveSkillRepository.findByCharacterAndEquippedTrue(
			character);

		return equippedSkills.stream()
			.map(EquippedPassiveSkillResponse::from)
			.collect(Collectors.toList());
	}

	@Override
	public void unequipPassiveSkillsNotMatchingCurrentJob(GameCharacter character) {
		List<CharacterPassiveSkill> equippedPassiveSkills = characterPassiveSkillRepository.findByCharacterAndEquippedTrue(
			character);
		for (CharacterPassiveSkill cs : equippedPassiveSkills) {
			if (!cs.getPassiveSkill().getJob().getJobCategory().equals(character.getJob().getJobCategory())) {
				cs.unequip();
			}
		}
	}

}
