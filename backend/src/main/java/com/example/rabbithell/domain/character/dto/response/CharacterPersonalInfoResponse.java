package com.example.rabbithell.domain.character.dto.response;

import com.example.rabbithell.domain.character.entity.Character;

public record CharacterPersonalInfoResponse(
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
    int stamina,
    int hp,
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
    Long cash,
    Long saving,
    int skillPoint,
    Long currentVillage
) implements CharacterInfoResponse {
    public static CharacterPersonalInfoResponse from(Character character) {
        return new CharacterPersonalInfoResponse(
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
            character.getStamina(),
            character.getHp(),
            character.getMp(),
            character.getStrength(),
            character.getAgility(),
            character.getIntelligence(),
            character.getFocus(),
            character.getLuck(),
            character.getWarriorPoint(),
            character.getThiefPoint(),
            character.getWizardPoint(),
            character.getArcherPoint(),
            character.getCash(),
            character.getSaving(),
            character.getSkillPoint(),
            character.getCurrentVillage()
        );
    }
}
