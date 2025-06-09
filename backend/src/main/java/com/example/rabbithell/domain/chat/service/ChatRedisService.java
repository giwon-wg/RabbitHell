package com.example.rabbithell.domain.chat.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.rabbithell.domain.chat.dto.response.ChatMessageResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRedisService {

	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;

	public List<ChatMessageResponseDto> getChatHistory(String roomId) {
		List<String> messages = redisTemplate.opsForList().range("chat:history:" + roomId, 0, -1);
		if (messages == null) return List.of();

		return messages.stream()
			.map(json -> {
				try {
					return objectMapper.readValue(json, ChatMessageResponseDto.class);
				} catch (JsonProcessingException e) {
					throw new RuntimeException("JSON 파싱 실패", e);
				}
			})
			.toList();
	}
}
