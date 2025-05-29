package com.example.rabbithell.domain.character.dto.response;

import com.example.rabbithell.domain.character.entity.Character;

public record CharacterPublicInfoResponse(
    Long id,
    Long userId,
    String userName,
    Long kingdomId,
    String kingdomName,
    Long speciesId,
    String speciesName,
    String name,
    String job,
    int level,
    int exp,
    int hp,
    int mp,
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
            // 유저 이름 받으면 수정 필요
            character.getUser().getEmail(),
            character.getKingdom().getId(),
            character.getKingdom().getKingdomName(),
            character.getSpecie().getId(),
            character.getSpecie().getSpeciesName(),
            character.getName(),
            character.getJob(),
            character.getLevel(),
            character.getExp(),
            character.getHp(),
            character.getMp(),
            character.getStrength(),
            character.getAgility(),
            character.getIntelligence(),
            character.getFocus(),
            character.getLuck()
        );
    }
}
