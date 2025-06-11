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
import com.example.rabbithell.domain.characterSkill.enums.SkillEquipType;
import com.example.rabbithell.domain.characterSkill.exception.CharacterSkillException;
import com.example.rabbithell.domain.characterSkill.repository.CharacterSkillRepository;
import com.example.rabbithell.domain.skill.entity.Skill;
import com.example.rabbithell.domain.skill.enums.SkillType;
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
	public void equipSkill(Long characterId, Long skillId, SkillEquipType slotType) {

		if (slotType == SkillEquipType.NONE) {
			throw new CharacterSkillException(INVALID_EQUIP_TYPE);
		}

		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);
		Skill skill = skillRepository.findByIdOrElseThrow(skillId);

		CharacterSkill characterSkill = characterSkillRepository.findByCharacterAndSkillOrElseThrow(character, skill);

		// 액티브 슬롯은 2개
		if (skill.getSkillType() == SkillType.ACTIVE) {
			long activeEquippedCount = characterSkillRepository.findByCharacter(character).stream()
				.filter(cs -> cs.isEquipped())
				.filter(cs -> cs.getEquipType() == SkillEquipType.ACTIVE_SLOT_1 || cs.getEquipType() == SkillEquipType.ACTIVE_SLOT_2)
				.count();

			if (activeEquippedCount >= 2 && characterSkill.getEquipType() == SkillEquipType.NONE) {
				throw new CharacterSkillException(MAX_ACTIVE_SLOTS_REACHED);
			}
		}

		// 패시브 슬롯은 1개
		if (skill.getSkillType() == SkillType.PASSIVE) {
			boolean passiveEquipped = characterSkillRepository.findByCharacter(character).stream()
				.anyMatch(cs -> cs.isEquipped() && cs.getEquipType() == SkillEquipType.PASSIVE_SLOT);

			if (passiveEquipped && characterSkill.getEquipType() == SkillEquipType.NONE) {
				throw new CharacterSkillException(MAX_PASSIVE_SLOTS_REACHED);
			}
		}

		if (character.getJob().getJobCategory() != skill.getJob().getJobCategory()) {
			throw new SkillException(INVALID_JOB_FOR_SKILL);
		}

		if (!character.getJob().getTier().isSameOrHigherThan(skill.getJob().getTier())) {
			throw new SkillException(INSUFFICIENT_TIER_FOR_SKILL);
		}

		// 스킬 액티브/패시브 확인
		if (skill.getSkillType().equals(SkillType.ACTIVE)) {
			if (!(slotType == SkillEquipType.ACTIVE_SLOT_1 || slotType == SkillEquipType.ACTIVE_SLOT_2)) {
				throw new CharacterSkillException(INVALID_EQUIP_TYPE);
			}
		} else if (skill.getSkillType().equals(SkillType.PASSIVE)) {
			if (slotType != SkillEquipType.PASSIVE_SLOT) {
				throw new CharacterSkillException(INVALID_EQUIP_TYPE);
			}
		} else {
			throw new CharacterSkillException(INVALID_EQUIP_TYPE);
		}

		// 같은 스킬이 이미 다른 슬롯에 장착되어 있는지 확인
		boolean isAlreadyEquippedInOtherSlot = characterSkillRepository.findByCharacter(character).stream()
			.filter(cs -> cs.isEquipped())
			.anyMatch(cs -> cs.getSkill().equals(skill) && cs.getEquipType() != slotType);

		if (isAlreadyEquippedInOtherSlot) {
			throw new CharacterSkillException(ALREADY_EQUIPPED_IN_OTHER_SLOT);
		}

		// 슬롯 중복 장착 방지 - 이미 해당 슬롯에 스킬 있으면 해제
		characterSkillRepository.findByCharacterAndEquipType(character, slotType)
			.ifPresent(cs -> cs.unequip());

		// 장착
		characterSkill.equip(slotType);
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

		List<CharacterSkill> equippedSkills = characterSkillRepository.findByCharacter(character).stream()
			.filter(CharacterSkill::isEquipped)
			.collect(Collectors.toList());

		return equippedSkills.stream()
			.map(EquippedSkillResponse::from)  // from(CharacterSkill)
			.collect(Collectors.toList());
	}

	@Transactional
	public void unequipSkillsNotMatchingCurrentJob(GameCharacter character) {
		List<CharacterSkill> equippedSkills = characterSkillRepository.findByCharacter(character).stream()
			.filter(CharacterSkill::isEquipped)
			.toList();

		for (CharacterSkill cs : equippedSkills) {
			if (!cs.getSkill().getJob().getJobCategory().equals(character.getJob().getJobCategory())) {
				cs.unequip();
			}
		}
	}
}
