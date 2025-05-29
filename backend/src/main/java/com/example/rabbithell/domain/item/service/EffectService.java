package com.example.rabbithell.domain.item.service;

import org.springframework.data.domain.Pageable;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.item.dto.request.EffectRequest;
import com.example.rabbithell.domain.item.dto.response.EffectResponse;

import jakarta.validation.Valid;

public interface EffectService {

    EffectResponse createEffect(@Valid EffectRequest effectRequest);

    EffectResponse getEffectById(Long effectId);

    PageResponse<EffectResponse> getAllEffects(Pageable pageable);

    EffectResponse updateEffect(Long effectId, @Valid EffectRequest effectRequest);

}
