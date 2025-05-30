package com.example.rabbithell.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Getter
public class ChatMessageDto {

	private final MessageType messageType;

	@NotBlank(message = "내용은 반드시 작성되어야 합니다.")
	private final String message;

	private final String username;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime createdAt;

	@JsonCreator
	public ChatMessageDto(@JsonProperty("chatUserId") Long chatUserId, @JsonProperty("messageType") MessageType messageType, @JsonProperty("message") String message, @JsonProperty("username") String username, @JsonProperty("createdAt") LocalDateTime createdAt) {

		this.messageType = messageType;
		this.message = message;
		this.username = username;
		this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
	}


	public static ChatMessageDto createEnterMessage(Long userId, String username) {
		return new ChatMessageDto(userId, MessageType.ENTER, username + "님이 입장하셨습니다.", username, LocalDateTime.now());
	}

	public static ChatMessageDto createChatMessage(Long userId, String username, String message) {
		return new ChatMessageDto(userId, MessageType.CHAT, message, username, LocalDateTime.now());
	}

	public static ChatMessageDto createQuitMessage(Long userId, String username) {
		return new ChatMessageDto(userId, MessageType.QUIT, username + "님이 퇴장하셨습니다.", username, LocalDateTime.now());
	}
}
