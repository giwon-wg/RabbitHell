package com.example.rabbithell.domain.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import com.example.rabbithell.domain.chat.dto.request.ChatMessageRequestDto;
import com.example.rabbithell.domain.chat.dto.response.ChatMessageResponseDto;
import com.example.rabbithell.domain.chat.exception.ChatMessageException;
import com.example.rabbithell.domain.chat.exception.ChatMessageExceptionCode;
import com.example.rabbithell.domain.chat.service.ChatMessageService;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.infrastructure.security.jwt.JwtUtil;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatMessageController {

	private final ChatMessageService chatMessageService;
	private final SimpMessagingTemplate messagingTemplate;
	private final JwtUtil jwtUtil;

	@MessageMapping("/chat/{roomId}")
	public void handleMessage(
		@DestinationVariable String roomId,
		SimpMessageHeaderAccessor accessor,
		@Payload ChatMessageRequestDto dto
	) {
		String token = (String) accessor.getSessionAttributes().get("jwtToken");

		if (!StringUtils.hasText(token) || !jwtUtil.validateToken(token)) {
			throw new ChatMessageException(ChatMessageExceptionCode.UNAUTHORIZED);
		}

		Claims claims = jwtUtil.parseClaims(token);
		Long userId = Long.valueOf(claims.getSubject());
		String username = claims.get("cloverName", String.class);

		if (!StringUtils.hasText(dto.message())) {
			throw new ChatMessageException(ChatMessageExceptionCode.NULL_MESSAGE);
		}

		chatMessageService.isOnCooldown(Long.valueOf(roomId), userId);
		chatMessageService.saveMessage(userId, dto.message());

		ChatMessageResponseDto responseDto =
			ChatMessageResponseDto.createChatMessage(username, dto.message());

		messagingTemplate.convertAndSend("/sub/chat/" + roomId, responseDto);
	}


	// 관리자 메시지 전송용 메서드
	@MessageMapping("/chat/{roomId}/admin")
	public void sendAdminMessage(
		@DestinationVariable Long roomId,
		ChatMessageRequestDto messageDto,
		@AuthenticationPrincipal User user
	) {
		validate(user, messageDto);

		try {
			// 관리자 권한 체크는 서비스 내부에서 처리됨
			ChatMessageResponseDto adminDto = ChatMessageResponseDto.createAdminMessage(user.getId(), messageDto.message());

			chatMessageService.sendAdminMessage(roomId, adminDto, user);

		} catch (ChatMessageException e) {
			throw e;
		} catch (Exception e) {
			throw new ChatMessageException(ChatMessageExceptionCode.MESSAGE_PROCESSING_ERROR);
		}
	}

	private void validate(User user, ChatMessageRequestDto messageDto) {
		if (user == null) {
			throw new ChatMessageException(ChatMessageExceptionCode.MESSAGE_PROCESSING_ERROR);
		}
		if (messageDto == null || !StringUtils.hasText(messageDto.message())) {
			throw new ChatMessageException(ChatMessageExceptionCode.INVALID_PAYLOAD);
		}
	}
}
