package com.example.rabbithell.domain.chat.config.messagebroker;

import org.redisson.api.listener.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.example.rabbithell.domain.chat.dto.response.ChatMessageResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener<String> {

	private final ObjectMapper objectMapper;
	private final SimpMessagingTemplate messagingTemplate;

	@Override
	public void onMessage(CharSequence channel, String message) {
		try {
			ChatMessageResponseDto dto = objectMapper.readValue(message, ChatMessageResponseDto.class);

			// channel 명에서 roomId 추출 (예: "chatroom:123")
			String channelStr = channel.toString();
			String roomId = channelStr.substring(channelStr.indexOf(":") + 1);

			// 클라이언트로 STOMP 브로드캐스트
			messagingTemplate.convertAndSend("/sub/chat/" + roomId, dto);

			log.info("📡 RedisSubscriber: /sub/chat/{} 채널로 메시지 전송 완료", roomId);
		} catch (Exception e) {
			log.error("❌ RedisSubscriber 메시지 처리 중 오류", e);
		}
	}

}
