package com.example.rabbithell.domain.chat.controller;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import com.example.rabbithell.domain.chat.config.messagebroker.RedisPublisher;
import com.example.rabbithell.domain.chat.dto.request.ChatMessageDeleteRequestDto;
import com.example.rabbithell.domain.chat.dto.request.ChatMessageRequestDto;
import com.example.rabbithell.domain.chat.dto.response.ChatMessageResponseDto;
import com.example.rabbithell.domain.chat.entity.ChatMessage;
import com.example.rabbithell.domain.chat.exception.ChatMessageException;
import com.example.rabbithell.domain.chat.exception.ChatMessageExceptionCode;
import com.example.rabbithell.domain.chat.internal.ChatRedisWriter;
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

	private final JwtTokenExtractor jwtTokenExtractor;
	private final JwtUtil jwtUtil;
	private final RedisPublisher redisPublisher;
	private final ChatMessageService chatMessageService;
	private final UserService userService;
	private final ChatRedisWriter chatRedisWriter;

	@MessageMapping("/chat/{roomId}")
	public void handleMessage(
		@DestinationVariable Long roomId,
		SimpMessageHeaderAccessor accessor,
		@Payload ChatMessageRequestDto dto
	) {
		try {
			// 1. JWT 추출
			String token = jwtTokenExtractor.extractToken(accessor);
			if (!StringUtils.hasText(token)) {
				throw new ChatMessageException(ChatMessageExceptionCode.UNAUTHORIZED);
			}

			// 2. JWT 유효성 검사 및 클레임 파싱
			if (!jwtUtil.validateToken(token)) {
				throw new ChatMessageException(ChatMessageExceptionCode.UNAUTHORIZED);
			}

			Claims claims = jwtUtil.parseClaims(token);
			Long userId = Long.valueOf(claims.getSubject());
			String cloverName = claims.get("cloverName", String.class);

			log.info("💬 받은 메시지: {}, roomId={}, cloverName={}", dto.message(), roomId, cloverName);
			log.debug("✅ 사용자 인증됨: userId={}, cloverName={}", userId, cloverName);

			// 3. 메시지 유효성 검사
			if (!StringUtils.hasText(dto.message())) {
				throw new ChatMessageException(ChatMessageExceptionCode.NULL_MESSAGE);
			}

			// 4. 쿨타임 확인 및 메시지 저장
			chatMessageService.isOnCooldown(roomId, userId);
			ChatMessage savedMessage = chatMessageService.saveMessage(roomId, userId, dto.message());

			// 5. 응답 DTO 생성
			ChatMessageResponseDto responseDto = new ChatMessageResponseDto(
				com.example.rabbithell.domain.chat.dto.MessageType.CHAT,
				savedMessage.getContents(),
				cloverName,
				LocalDateTime.now()
			);

			// 6. Redis로 브로드캐스트 및 저장
			redisPublisher.publish(roomId, responseDto);
			chatRedisWriter.saveChatMessage(String.valueOf(roomId), responseDto);

		} catch (ChatMessageException e) {
			log.error("❗️채팅 메시지 예외 발생: {}", e.getMessage());
			throw new ChatMessageException(ChatMessageExceptionCode.MESSAGE_PROCESSING_ERROR);
		} catch (Exception e) {
			log.error("❗️예상치 못한 오류 발생", e);
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
