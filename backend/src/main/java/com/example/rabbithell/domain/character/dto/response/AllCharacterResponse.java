package com.example.rabbithell.domain.character.dto.response;

public record AllCharacterResponse(
	Long cloverId,
	String cloverName,
	Long characterId,
    String characterName,
    String job,
    int level,
    int maxHp,
    int maxMp,
    int strength,
    int agility,
    int intelligence,
    int focus,
    int luck
) {
}
