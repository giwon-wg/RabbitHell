package com.example.rabbithell.domain.deck.service;

import org.springframework.stereotype.Service;

import com.example.rabbithell.common.service.RedisService;
import com.example.rabbithell.domain.deck.dto.PawCardEffectDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PawCardEffectRedisService {

	private final RedisService redisService;
	private final PawCardEffectQueryService pawCardEffectQueryService;
	private static final String EFFECT_KEY_PREFIX = "effect:";

	public void saveEffect(Long cloverId, PawCardEffectDto pawCardEffectDto) {
		String key = EFFECT_KEY_PREFIX + cloverId;
		redisService.set(key, pawCardEffectDto);
	}

	public PawCardEffectDto getEffect(Long cloverId) {
		String key = EFFECT_KEY_PREFIX + cloverId;
		PawCardEffectDto cached = redisService.get(key, PawCardEffectDto.class);
		if (cached != null) {
			return cached;
		}
		PawCardEffectDto pawCardEffectDto = pawCardEffectQueryService.getEffectOnly(cloverId);
		redisService.set(EFFECT_KEY_PREFIX + cloverId, pawCardEffectDto);
		return pawCardEffectDto;
	}

	public void deleteEffect(Long cloverId) {
		redisService.delete(EFFECT_KEY_PREFIX + cloverId);
	}
}
