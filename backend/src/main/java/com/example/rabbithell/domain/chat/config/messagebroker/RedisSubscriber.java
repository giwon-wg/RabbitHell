package com.example.rabbithell.domain.chat.config.messagebroker;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import com.example.rabbithell.domain.chat.dto.response.ChatMessageResponseDto;
import com.example.rabbithell.domain.chat.exception.ChatMessageException;
import com.example.rabbithell.domain.chat.exception.ChatMessageExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSubscriber implements org.springframework.data.redis.connection.MessageListener {

	private final ObjectMapper objectMapper;
	private final SimpMessagingTemplate messagingTemplate;

	@Override
	public void onMessage(org.springframework.data.redis.connection.Message message, byte[] pattern) {
		try {
			// 1. 채널명: "chatroom:1"
			String channelName = new String(message.getChannel());
			// 2. 메시지 내용: JSON 문자열
			String json = new String(message.getBody());

			// 3. roomId 추출
			String roomId = extractRoomId(channelName);

			// 4. JSON 역직렬화
			ChatMessageResponseDto dto = objectMapper.readValue(json, ChatMessageResponseDto.class);

			// 5. WebSocket으로 전송
			messagingTemplate.convertAndSend("/sub/chat/" + roomId, dto);
			log.info("RedisSubscriber: 메시지 전송 완료 /sub/chat/{}", roomId);

		} catch (Exception e) {
			log.error("RedisSubscriber 메시지 처리 오류", e);
			throw new ChatMessageException(ChatMessageExceptionCode.REDIS_MESSAGE_ERROR);
		}
	}

	private String extractRoomId(String channelName) {
		if (channelName.startsWith("chatroom:")) {
			return channelName.substring("chatroom:".length());
		}
		log.warn("예상치 못한 채널명 형식: {}", channelName);
		throw new ChatMessageException(ChatMessageExceptionCode.ROOM_NUMBER_ERROR);
	}
}
