package com.example.rabbithell.domain.character.dto.response;

public record AllCharacterResponse(
    Long userId,
    String userName,
    Long characterId,
    String characterName,
    Long kingdomId,
    String kingdomName,
    Long speciesId,
    String speciesName,
    String job,
    int level,
    int stamina,
    int maxHp,
    int maxMp,
    int strength,
    int agility,
    int intelligence,
    int focus,
    int luck,
    Long currentVillage
) {
}
