package com.example.rabbithell.domain.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatMessageController {

	private final ChatMessageService chatMessageService;
	private final SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/websocket/chat/{roomId}")
	public void sendChatMessage(
		@DestinationVariable Long roomId,
		ChatMessageRequestDto messageDto,
//		@AuthenticationPrincipal User user //websecurity 기반 http , 인증인가-> 인터셉터에서 하는걸로
// 		websocket 실행 테스트 postman X
		User user
	) {
		validate(user, messageDto);

		String username = user.getName();
		Long userId = user.getId();

		try {
			if (!StringUtils.hasText(messageDto.message())) {
				throw new ChatMessageException(ChatMessageExceptionCode.NULL_MESSAGE);
			}

			// 쿨타임 체크
			chatMessageService.isOnCooldown(roomId, userId);

			// 저장 (message만 넘김)
			chatMessageService.saveMessage(userId, messageDto.message());

			// 메시지 전송 - 경로 통일 (/sub/chat/)
			ChatMessageResponseDto finalMessage = ChatMessageResponseDto.createChatMessage(username, messageDto.message());
			messagingTemplate.convertAndSend("/sub/chat/" + roomId, finalMessage);

		} catch (ChatMessageException e) {
			throw e;
		} catch (Exception e) {
			throw new ChatMessageException(ChatMessageExceptionCode.MESSAGE_PROCESSING_ERROR);
		}
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
