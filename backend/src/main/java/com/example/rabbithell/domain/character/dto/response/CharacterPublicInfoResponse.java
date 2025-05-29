package com.example.rabbithell.domain.character.dto.response;

import com.example.rabbithell.domain.character.entity.Character;

public record CharacterPublicInfoResponse(
    Long characterId,
    Long userId,
    String userName,
    Long kingdomId,
    String kingdomName,
    Long speciesId,
    String speciesName,
    String characterName,
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
    public static CharacterPublicInfoResponse from(Character character){
        return new CharacterPublicInfoResponse(
            character.getId(),
            character.getUser().getId(),
            character.getUser().getName(),
            character.getKingdom().getId(),
            character.getKingdom().getKingdomName(),
            character.getSpecie().getId(),
            character.getSpecie().getSpeciesName(),
            character.getName(),
            character.getJob(),
            character.getLevel(),
            character.getExp(),
            character.getMaxHp(),
            character.getMaxMp(),
            character.getStrength(),
            character.getAgility(),
            character.getIntelligence(),
            character.getFocus(),
            character.getLuck()
        );
    }
}
