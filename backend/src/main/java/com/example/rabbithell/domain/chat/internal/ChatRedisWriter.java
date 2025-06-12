package com.example.rabbithell.domain.chat.internal;

import com.example.rabbithell.domain.chat.dto.response.ChatMessageResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRedisWriter {

	//redis 저장 책임 분리용
	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;

	public void saveChatMessage(String roomId, ChatMessageResponseDto message) {
		try {
			redisTemplate.opsForValue().set("1","sdsdsd");
			String key = "chat:history:" + roomId;
			String json = objectMapper.writeValueAsString(message);

			redisTemplate.opsForList().rightPush(key, json);
			log.info(" Redis 저장 성공");
			// 선택: 최근 100개까지만 유지
			redisTemplate.opsForList().trim(key, -100, -1);

		} catch (JsonProcessingException e) {
			log.error(" Redis 저장 실패: 직렬화 에러", e);
		}
	}
}
