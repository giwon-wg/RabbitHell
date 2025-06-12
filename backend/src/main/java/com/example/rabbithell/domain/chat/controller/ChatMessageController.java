package com.example.rabbithell.domain.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import com.example.rabbithell.domain.chat.config.messagebroker.RedisPublisher;
import com.example.rabbithell.domain.chat.dto.request.ChatMessageDeleteRequestDto;
import com.example.rabbithell.domain.chat.dto.request.ChatMessageRequestDto;
import com.example.rabbithell.domain.chat.dto.response.ChatMessageResponseDto;
import com.example.rabbithell.domain.chat.entity.ChatMessage;
import com.example.rabbithell.domain.chat.exception.ChatMessageException;
import com.example.rabbithell.domain.chat.exception.ChatMessageExceptionCode;
import com.example.rabbithell.domain.chat.service.ChatMessageService;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.service.UserService;
import com.example.rabbithell.infrastructure.security.jwt.JwtTokenExtractor;
import com.example.rabbithell.infrastructure.security.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

	private final ChatMessageService chatMessageService;
	private final UserService userService;
	private final SimpMessagingTemplate messagingTemplate;
	private final JwtUtil jwtUtil;
	private final JwtTokenExtractor jwtTokenExtractor;
	private final RedisPublisher redisPublisher;
	private final ObjectMapper objectMapper;


	@MessageMapping("/chat/{roomId}")
	public void handleMessage(
		@DestinationVariable Long roomId,
		SimpMessageHeaderAccessor accessor,
		@Payload ChatMessageRequestDto dto
	) {
		try {
			log.info("💬 {}님의 메시지: {}", dto.sender(), dto.message());
			log.info("📌 roomId = {}", roomId);
			// JWT 토큰 추출 - 여러 방법 시도
			String token = jwtTokenExtractor.extractToken(accessor);

			if (!StringUtils.hasText(token)) {
				log.warn("JWT 토큰이 없습니다. 세션 속성: {}", accessor.getSessionAttributes());
				throw new ChatMessageException(ChatMessageExceptionCode.UNAUTHORIZED);
			}

			if (!jwtUtil.validateToken(token)) {
				log.warn("유효하지 않은 JWT 토큰입니다. 토큰: {}", token.substring(0, Math.min(20, token.length())) + "...");
				throw new ChatMessageException(ChatMessageExceptionCode.UNAUTHORIZED);
			}

			Claims claims = jwtUtil.parseClaims(token);
			Long userId = Long.valueOf(claims.getSubject());
			String username = claims.get("cloverName", String.class);

			log.debug("인증된 사용자: ID={}, Username={}", userId, username);

			if (!StringUtils.hasText(dto.message())) {
				throw new ChatMessageException(ChatMessageExceptionCode.NULL_MESSAGE);
			}

			chatMessageService.isOnCooldown(Long.valueOf(roomId), userId);
			ChatMessage filteredmessage = chatMessageService.saveMessage(roomId, userId, dto.message());

			//todo: dto에서 username 변수로 재설정 해주기

			ChatMessageResponseDto responseDto = ChatMessageResponseDto.createChatMessage(username, filteredmessage);

			// ✅ Redis 발행으로 전환
			String json = objectMapper.writeValueAsString(responseDto);
			redisPublisher.publish(roomId, responseDto); // 또는 "chat-room." + roomId 도 가능

		} catch (ChatMessageException e) {
			log.error("채팅 메시지 처리 중 오류 발생: {}", e.getMessage());
			throw new ChatMessageException(ChatMessageExceptionCode.MESSAGE_PROCESSING_ERROR);
		} catch (Exception e) {
			log.error("예상치 못한 오류 발생", e);
			throw new ChatMessageException(ChatMessageExceptionCode.MESSAGE_PROCESSING_ERROR);
		}

	}

	@MessageMapping("/chat/{roomId}/admin/notice")
	public void sendAdminNotice(
		@DestinationVariable Long roomId,
		SimpMessageHeaderAccessor accessor,
		@Payload ChatMessageRequestDto dto
	) {
		try {
			User admin = userService.getUserFromAccessor(accessor);

			if (!StringUtils.hasText(dto.message())) {
				throw new ChatMessageException(ChatMessageExceptionCode.NULL_MESSAGE);
			}

			ChatMessageResponseDto noticeDto = ChatMessageResponseDto.createAdminMessage(
				admin.getId(),
				dto.message()
			);

			chatMessageService.sendAdminMessage(roomId, noticeDto, admin);

		} catch (ChatMessageException e) {
			log.error("관리자 공지 처리 중 오류: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("예상치 못한 관리자 공지 오류", e);
			throw new ChatMessageException(ChatMessageExceptionCode.MESSAGE_PROCESSING_ERROR);
		}
	}


	@MessageMapping("/chat/{roomId}/admin/delete")
	public void deleteUserMessage(
		@DestinationVariable Long roomId,
		SimpMessageHeaderAccessor accessor,
		@Payload ChatMessageDeleteRequestDto dto
	) {
		try {
			User admin = userService.getUserFromAccessor(accessor);
			chatMessageService.sendMessageDeletedNotification(roomId, dto.messageId(), admin);
		} catch (ChatMessageException e) {
			throw e;
		} catch (Exception e) {
			log.error("메시지 삭제 중 오류 발생: roomId={}, messageId={}", roomId, dto.messageId(), e);
			throw new ChatMessageException(ChatMessageExceptionCode.MESSAGE_DELETE_ERROR);
		}
	}

}
