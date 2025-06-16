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
import com.example.rabbithell.domain.characterSkill.dto.response.EquippedActiveSkillResponse;
import com.example.rabbithell.domain.characterSkill.dto.response.LearnableActiveSkillResponse;
import com.example.rabbithell.domain.characterSkill.dto.response.LearnedActiveSkillResponse;
import com.example.rabbithell.domain.characterSkill.entity.CharacterActiveSkill;
import com.example.rabbithell.domain.characterSkill.enums.SkillEquipType;
import com.example.rabbithell.domain.characterSkill.exception.CharacterSkillException;
import com.example.rabbithell.domain.characterSkill.repository.CharacterActiveSkillRepository;
import com.example.rabbithell.domain.skill.entity.ActiveSkill;
import com.example.rabbithell.domain.skill.exception.SkillException;
import com.example.rabbithell.domain.skill.repository.ActiveSkillRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CharacterActiveSkillServiceImpl implements CharacterActiveSkillService {

	private final CharacterRepository characterRepository;
	private final CharacterActiveSkillRepository characterActiveSkillRepository;
	private final ActiveSkillRepository activeSkillRepository;

	@Override
	@Transactional
	public void learnActiveSkill(Long characterId, Long skillId) {
		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);
		ActiveSkill activeSkill = activeSkillRepository.findByIdOrElseThrow(skillId);

		if (characterActiveSkillRepository.existsByCharacterAndActiveSkill(character, activeSkill)) {
			throw new SkillException(ALREADY_LEARNED);
		}

		if (character.getJob().getJobCategory() != activeSkill.getJob().getJobCategory()) {
			throw new SkillException(INVALID_JOB_FOR_SKILL);
		}

		if (!character.getJob().getTier().isSameOrHigherThan(activeSkill.getJob().getTier())) {
			throw new SkillException(INSUFFICIENT_TIER_FOR_SKILL);
		}

		character.learnSkillBySkillPoint(activeSkill.getRequiredSkillPoint());
		characterRepository.save(character);

		CharacterActiveSkill characterActiveSkill = new CharacterActiveSkill(character, activeSkill);
		characterActiveSkillRepository.save(characterActiveSkill);
	}

	@Override
	@Transactional
	public void equipActiveSkill(Long characterId, Long skillId, SkillEquipType slotType) {

		if (slotType == SkillEquipType.NONE) {
			throw new CharacterSkillException(INVALID_EQUIP_TYPE);
		}

		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);
		ActiveSkill activeSkill = activeSkillRepository.findByIdOrElseThrow(skillId);

		CharacterActiveSkill characterActiveSkill = characterActiveSkillRepository.findByCharacterAndActiveSkillOrElseThrow(
			character,
			activeSkill);

		if (character.getJob().getJobCategory() != activeSkill.getJob().getJobCategory()) {
			throw new SkillException(INVALID_JOB_FOR_SKILL);
		}

		if (!character.getJob().getTier().isSameOrHigherThan(activeSkill.getJob().getTier())) {
			throw new SkillException(INSUFFICIENT_TIER_FOR_SKILL);
		}

		long equippedCount = characterActiveSkillRepository.findByCharacter(character)
			.stream()
			.filter(cs -> cs.isEquipped())
			.filter(cs -> cs.getEquipType() == SkillEquipType.ACTIVE_SLOT_1
				|| cs.getEquipType() == SkillEquipType.ACTIVE_SLOT_2)
			.count();

		if (equippedCount >= 2 && characterActiveSkill.getEquipType() == SkillEquipType.NONE) {
			throw new CharacterSkillException(MAX_ACTIVE_SLOTS_REACHED);
		}

		// 다른 슬롯에 이미 장착된 동일 스킬인지 확인
		boolean alreadyEquippedElsewhere = characterActiveSkillRepository.findByCharacter(character).stream()
			.filter(CharacterActiveSkill::isEquipped)
			.anyMatch(cs -> cs.getActiveSkill().equals(activeSkill) && cs.getEquipType() != slotType);

		if (alreadyEquippedElsewhere) {
			throw new CharacterSkillException(ALREADY_EQUIPPED_IN_OTHER_SLOT);
		}

		// 해당 슬롯에 이미 스킬이 있다면 해제
		characterActiveSkillRepository.findByCharacterAndEquipType(character, slotType)
			.ifPresent(CharacterActiveSkill::unequip);

		characterActiveSkill.equip(slotType);
	}

	@Override
	@Transactional
	public void unequipActiveSkill(Long characterId, Long skillId) {

		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);

		ActiveSkill activeSkill = activeSkillRepository.findByIdOrElseThrow(skillId);

		CharacterActiveSkill characterActiveSkill = characterActiveSkillRepository.findByCharacterAndActiveSkillOrElseThrow(
			character,
			activeSkill);

		if (!characterActiveSkill.isEquipped()) {
			throw new CharacterSkillException(ALREADY_UNEQUIPPED);
		}

		characterActiveSkill.unequip();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<LearnedActiveSkillResponse> getLearnedActiveSkills(Long characterId, Pageable pageable) {
		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);

		Page<CharacterActiveSkill> learnedSkills = characterActiveSkillRepository.findByCharacter(character, pageable);

		return learnedSkills.map(LearnedActiveSkillResponse::from);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<LearnableActiveSkillResponse> getLearnableActiveSkills(Long characterId, Pageable pageable) {
		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);

		List<Long> learnedSkillIds = characterActiveSkillRepository.findSkillIdsByCharacter(character);

		Page<ActiveSkill> skills = activeSkillRepository.findLearnableSkills(
			character.getJob().getJobCategory(),
			character.getJob().getTier().getTier(),
			learnedSkillIds,
			pageable
		);

		return skills.map(skill -> new LearnableActiveSkillResponse(
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
	public List<EquippedActiveSkillResponse> getEquippedActiveSkills(Long characterId) {
		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);

		List<CharacterActiveSkill> equippedSkills = characterActiveSkillRepository.findByCharacter(character).stream()
			.filter(CharacterActiveSkill::isEquipped)
			.collect(Collectors.toList());

		return equippedSkills.stream()
			.map(EquippedActiveSkillResponse::from)
			.collect(Collectors.toList());
	}

	@Transactional
	public void unequipActiveSkillsNotMatchingCurrentJob(GameCharacter character) {
		List<CharacterActiveSkill> equippedActiveSkills = characterActiveSkillRepository.findByCharacter(character)
			.stream()
			.filter(CharacterActiveSkill::isEquipped)
			.toList();

		for (CharacterActiveSkill cs : equippedActiveSkills) {
			if (!cs.getActiveSkill().getJob().getJobCategory().equals(character.getJob().getJobCategory())) {
				cs.unequip();
			}
		}
	}
}
