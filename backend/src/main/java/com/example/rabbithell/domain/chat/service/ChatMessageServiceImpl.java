package com.example.rabbithell.domain.chat.service;

import com.example.rabbithell.domain.chat.dto.ChatMessageDto;
import com.example.rabbithell.domain.chat.entity.ChatMessage;
import com.example.rabbithell.domain.chat.repository.ChatMessageRepository;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

	private final ChatMessageRepository chatMessageRepository;
	private final UserRepository userRepository;

	@Override
	public void saveMessage(Long roomId, ChatMessageDto dto) {

		User user = userRepository.findById(dto.getChatUserId()).orElseThrow(() -> new RuntimeException("사용자 없음"));

		ChatMessage message = new ChatMessage();
		message.setUser(user);
		message.setContents(dto.getMessage());
		message.setCreatedAt(LocalDateTime.now());

		chatMessageRepository.save(message);

	}
}
