package com.example.rabbithell.domain.character.dto.response;

import java.util.List;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.characterSkill.dto.response.EquippedSkillResponse;
import com.example.rabbithell.domain.characterSkill.dto.response.LearnedSkillResponse;

public record CharacterPersonalInfoResponse(
	Long cloverId,
	String cloverName,
	String kingdomName,
	String speciesName,
	String characterName,
	String job,
	int level,
	int exp,
	int maxHp,
	int hp,
	int maxMp,
	int mp,
	int strength,
	int agility,
	int intelligence,
	int focus,
	int luck,
	int warriorPoint,
	int thiefPoint,
	int wizardPoint,
	int archerPoint,
	int skillPoint
	// List<LearnedSkillResponse> learnedSkills,
	// List<EquippedSkillResponse> equippedSkills
) implements CharacterInfoResponse {
	public static CharacterPersonalInfoResponse from(
		GameCharacter gameCharacter
		// List<LearnedSkillResponse> learnedSkills,
		// List<EquippedSkillResponse> equippedSkills
	) {
		return new CharacterPersonalInfoResponse(
			gameCharacter.getClover().getId(),
			gameCharacter.getClover().getName(),
			gameCharacter.getClover().getKingdom().getKingdomName(),
			gameCharacter.getClover().getSpecie().getSpeciesName(),
			gameCharacter.getName(),
			gameCharacter.getJob().getName(),
			gameCharacter.getLevel(),
			gameCharacter.getExp(),
			gameCharacter.getMaxHp(),
			gameCharacter.getHp(),
			gameCharacter.getMaxMp(),
			gameCharacter.getMp(),
			gameCharacter.getStrength(),
			gameCharacter.getAgility(),
			gameCharacter.getIntelligence(),
			gameCharacter.getFocus(),
			gameCharacter.getLuck(),
			gameCharacter.getWarriorPoint(),
			gameCharacter.getThiefPoint(),
			gameCharacter.getWizardPoint(),
			gameCharacter.getArcherPoint(),
			gameCharacter.getSkillPoint()
			// learnedSkills,
			// equippedSkills
		);
	}

}
