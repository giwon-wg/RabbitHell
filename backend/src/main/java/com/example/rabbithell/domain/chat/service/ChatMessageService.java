package com.example.rabbithell.domain.chat.service;

import com.example.rabbithell.domain.chat.dto.request.ChatMessageRequestDto;
import com.example.rabbithell.domain.chat.dto.response.ChatMessageResponseDto;
import com.example.rabbithell.domain.chat.entity.ChatMessage;
import com.example.rabbithell.domain.user.model.User;


public interface ChatMessageService {
//	void saveMessage(Long roomId, String message);

	ChatMessage saveMessage(Long roomId, Long userId, String message);

	void deleteMessage(Long roomId, Long chatMessageId);

	void isOnCooldown(Long roomId, Long userId);

	void sendAdminMessage(Long roomId, ChatMessageResponseDto dto, User adminUser);
}
