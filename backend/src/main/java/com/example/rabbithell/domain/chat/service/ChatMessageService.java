package com.example.rabbithell.domain.chat.service;

import com.example.rabbithell.domain.chat.dto.ChatMessageDto;
import com.example.rabbithell.domain.user.model.User;

import jakarta.validation.constraints.NotBlank;

import org.springframework.stereotype.Service;


public interface ChatMessageService {
	void saveMessage(Long roomId, ChatMessageDto dto);
}
