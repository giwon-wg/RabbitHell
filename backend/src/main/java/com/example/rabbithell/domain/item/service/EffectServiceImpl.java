package com.example.rabbithell.domain.item.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.common.dto.response.PageResponse;
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

    @Transactional(readOnly = true)
    @Override
    public EffectResponse getEffectById(Long effectId) {
        Effect effect = effectRepository.findByIdOrElseThrow(effectId);
        return EffectResponse.fromEntity(effect);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<EffectResponse> getAllEffects(Pageable pageable) {
        Page<Effect> page = effectRepository.findAll(pageable);

        List<EffectResponse> dtoList = page.getContent().stream()
            .map(EffectResponse::fromEntity)
            .toList();

        return PageResponse.of(dtoList, page);
    }

    @Transactional
    @Override
    public EffectResponse updateEffect(Long effectId, EffectRequest effectRequest) {
        Effect effect = effectRepository.findByIdOrElseThrow(effectId);

        effect.update(effectRequest.effectType(), effectRequest.power());

        return EffectResponse.fromEntity(effect);
    }

    @Transactional
    @Override
    public void deleteEffect(Long effectId) {
        Effect effect = effectRepository.findByIdOrElseThrow(effectId);
        effect.markAsDeleted();
    }

}
