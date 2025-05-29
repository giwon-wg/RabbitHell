package com.example.rabbithell.domain.item.service;

import org.springframework.stereotype.Service;

import com.example.rabbithell.domain.item.dto.request.EffectRequest;
import com.example.rabbithell.domain.item.dto.response.EffectResponse;
import com.example.rabbithell.domain.item.entity.Effect;
import com.example.rabbithell.domain.item.repository.EffectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EffectServiceImpl implements EffectService {

    private final EffectRepository effectRepository;

    @Override
    public EffectResponse createEffect(EffectRequest effectRequest) {
        Effect effect = Effect.builder()
            .effectType(effectRequest.effectType())
            .power(effectRequest.power())
            .build();

        Effect savedEffect = effectRepository.save(effect);
        return EffectResponse.fromEntity(savedEffect);
    }

}
