package com.example.rabbithell.domain.item.service;

import com.example.rabbithell.domain.item.dto.request.EffectRequest;
import com.example.rabbithell.domain.item.dto.response.EffectResponse;

import jakarta.validation.Valid;

public interface EffectService {

    EffectResponse createEffect(@Valid EffectRequest effectRequest);

    EffectResponse getEffectById(Long effectId);

    EffectResponse updateEffect(Long effectId, @Valid EffectRequest effectRequest);

}
