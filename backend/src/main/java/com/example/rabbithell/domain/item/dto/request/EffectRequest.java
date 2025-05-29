package com.example.rabbithell.domain.item.dto.request;

import com.example.rabbithell.domain.item.enums.EffectType;

public record EffectRequest(
    EffectType effectType,
    Long power
) {}
