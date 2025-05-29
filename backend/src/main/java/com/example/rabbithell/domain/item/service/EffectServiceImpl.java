package com.example.rabbithell.domain.item.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public EffectResponse updateEffect(Long effectId, EffectRequest effectRequest) {
        Effect effect = effectRepository.findByIdOrElseThrow(effectId);

        effect.update(effectRequest.effectType(), effectRequest.power());

        return EffectResponse.fromEntity(effect);
    }

}
