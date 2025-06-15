package com.example.rabbithell.domain.deck.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.rabbithell.common.service.RedisService;
import com.example.rabbithell.domain.deck.dto.DeckRedisDto;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeckRedisService {

	private final RedisService redisService;
	private static final String DECK_KEY_PREFIX = "deck:";

	public void saveDecks(Long cloverId, List<DeckRedisDto> deckRedisDtoList) {
		String key = DECK_KEY_PREFIX + cloverId;
		redisService.set(key, deckRedisDtoList);
	}

	public List<DeckRedisDto> getDecks(Long cloverId) {
		String key = DECK_KEY_PREFIX + cloverId;
		return redisService.get(key, new TypeReference<List<DeckRedisDto>>() {
		});
	}

	public void deleteDecks(Long cloverId) {
		redisService.delete(DECK_KEY_PREFIX + cloverId);
	}
}
