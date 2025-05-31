package com.example.rabbithell.domain.character.dto.response;

import com.example.rabbithell.domain.character.entity.GameCharacter;

public record CharacterPublicInfoResponse(
	Long userId,
	String userName,
	Long cloverId,
	String cloverName,
	Long characterId,
	String characterName,
	String kingdomName,
	String speciesName,
    String job,
    int level,
    int exp,
    int maxHp,
    int maxMp,
    int strength,
    int agility,
    int intelligence,
    int focus,
    int luck
) implements CharacterInfoResponse {
    public static CharacterPublicInfoResponse from(GameCharacter gameCharacter){
        return new CharacterPublicInfoResponse(
			gameCharacter.getUser().getId(),
			gameCharacter.getUser().getName(),
			gameCharacter.getClover().getId(),
			gameCharacter.getClover().getName(),
			gameCharacter.getId(),
			gameCharacter.getName(),
			"왕국이름",
			"종족명",
			// gameCharacter.getClover().getKingdom().getKingdomName(),
			// gameCharacter.getClover().getSpecie().getSpeciesName(),
            gameCharacter.getJob().getName(),
            gameCharacter.getLevel(),
            gameCharacter.getExp(),
            gameCharacter.getMaxHp(),
            gameCharacter.getMaxMp(),
            gameCharacter.getStrength(),
            gameCharacter.getAgility(),
            gameCharacter.getIntelligence(),
            gameCharacter.getFocus(),
            gameCharacter.getLuck()
        );
    }
}
