package com.example.rabbithell.domain.chat.controller;

import com.example.rabbithell.domain.chat.dto.ChatMessageDto;
import com.example.rabbithell.domain.chat.exception.ChatMessageException;
import com.example.rabbithell.domain.chat.exception.ChatMessageExceptionCode;
import com.example.rabbithell.domain.chat.service.ChatMessageService;
import com.example.rabbithell.domain.user.model.User;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatMessageController {

	private final ChatMessageService chatMessageService;
	private final SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/chat/{roomId}")
	public void sendChatMessage(@DestinationVariable Long roomId, ChatMessageDto messageDto, @AuthenticationPrincipal User user) {
		if (user == null) {
			throw new ChatMessageException(ChatMessageExceptionCode.MESSAGE_PROCESSING_ERROR);
		}

		if (messageDto == null || messageDto.getMessageType() == null) {
			throw new ChatMessageException(ChatMessageExceptionCode.INVALID_PAYLOAD);
		}

		String username = user.getName();
		Long userId = user.getId();

		try {
			ChatMessageDto finalMessage;

			switch (messageDto.getMessageType()) {
				case ENTER -> {
					finalMessage = ChatMessageDto.createEnterMessage(userId, username);
				}
				case CHAT -> {
					if (!StringUtils.hasText(messageDto.getMessage())) {
						throw new ChatMessageException(ChatMessageExceptionCode.NULL_MESSAGE);
					}
					chatMessageService.saveMessage(roomId, messageDto);
					finalMessage = ChatMessageDto.createChatMessage(userId, username, messageDto.getMessage());
				}
				case QUIT -> {
					finalMessage = ChatMessageDto.createQuitMessage(userId, username);
				}
				default -> throw new ChatMessageException(ChatMessageExceptionCode.INVALID_PAYLOAD);
			}

			messagingTemplate.convertAndSend("/sub/room/" + roomId, finalMessage);

		} catch (ChatMessageException e) {
			throw e;
		} catch (Exception e) {
			throw new ChatMessageException(ChatMessageExceptionCode.MESSAGE_PROCESSING_ERROR);
		}
	}
}
