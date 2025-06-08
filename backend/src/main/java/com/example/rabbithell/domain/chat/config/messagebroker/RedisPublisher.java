package com.example.rabbithell.domain.chat.config.messagebroker;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.example.rabbithell.domain.chat.dto.response.ChatMessageResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisPublisher {

	private final StringRedisTemplate redisTemplate;
	private final ObjectMapper objectMapper;

	public void publish(Long roomId, ChatMessageResponseDto messageDto) {
		try {
			String json = objectMapper.writeValueAsString(messageDto);
			String topic = "chatroom:" + roomId;
			redisTemplate.convertAndSend(topic, json);
			log.info("📤 RedisPublisher: {}에 메시지 발행 완료", topic);
		} catch (JsonProcessingException e) {
			log.error("RedisPublisher 직렬화 실패", e);
		}
	}

}
