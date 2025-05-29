package com.example.rabbithell.domain.character.dto.request;

public record CreateCharacterRequest(
    Long kingdomId,
    Long speciesId,
    String name
) {
}
