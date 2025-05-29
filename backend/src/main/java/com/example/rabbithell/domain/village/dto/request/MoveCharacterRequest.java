package com.example.rabbithell.domain.village.dto.request;

import jakarta.validation.constraints.NotNull;

public record MoveCharacterRequest(
    @NotNull Long characterId,
    @NotNull Long targetVillageId
) {}
