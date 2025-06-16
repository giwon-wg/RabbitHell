package com.example.rabbithell.domain.util.skillvalidatelogic;

import static com.example.rabbithell.domain.skill.exception.code.SkillExceptionCode.*;

import java.util.List;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.characterSkill.entity.CharacterPassiveSkill;
import com.example.rabbithell.domain.characterSkill.enums.SkillEquipType;
import com.example.rabbithell.domain.job.entity.JobCategory;
import com.example.rabbithell.domain.skill.entity.PassiveSkill;
import com.example.rabbithell.domain.skill.exception.SkillException;

public class SkillValidateLogic {

	// 공통: 직업군, 티어 체크
	public static void validateJobCategory(GameCharacter character, JobCategory skillJobCategory) {
		if (!character.getJob().getJobCategory().equals(skillJobCategory)) {
			throw new SkillException(INVALID_JOB_FOR_SKILL);
		}
	}

	public static void validateTier(GameCharacter character, int requiredTier) {
		if (character.getJob().getTier().getTier() < requiredTier) {
			throw new SkillException(INSUFFICIENT_TIER_FOR_SKILL);
		}
	}

	// 액티브 스킬 관련 유효성 검사
	public static void validateActiveSkillPoint(GameCharacter character, int cost) {
		if (character.getSkillPoint() < cost) {
			throw new SkillException(NOT_ENOUGH_SKILL_POINTS);
		}
	}

	public static void validateActiveEquipSlot(SkillEquipType equipSlot) {
		if (equipSlot == null) {
			throw new SkillException(NOT_ENOUGH_SKILL_SLOTS);
		}
		// 추가로 유효하지 않은 슬롯인지 체크할 수 있으면 아래처럼
		// if (!isValidEquipSlot(equipSlot)) {
		//     throw new SkillException(INVALID_EQUIP_SLOT);
		// }
	}

	// 패시브 스킬 관련 유효성 검사
	public static void validatePassiveDuplicate(List<CharacterPassiveSkill> passiveSkills, PassiveSkill newSkill) {
		boolean alreadyLearned = passiveSkills.stream()
			.anyMatch(cps -> cps.getPassiveSkill().getId().equals(newSkill.getId()));

		if (alreadyLearned) {
			throw new SkillException(ALREADY_LEARNED);
		}
	}

	public static void validatePassiveSlotLimit(List<CharacterPassiveSkill> passiveSkills ) {
		if (passiveSkills.size() > 2) {
			throw new SkillException(NOT_ENOUGH_SKILL_SLOTS);
		}
	}

	// 중복 장착 방지
	public static void validateAlreadyEquipped(boolean isEquipped) {
		if (isEquipped) {
			throw new SkillException(ALREADY_EQUIPPED);
		}
	}

	// 스킬 배웠는지 체크
	public static void checkLearned(boolean learned) {
		if (!learned) {
			throw new SkillException(NOT_LEARNED);
		}
	}
}
