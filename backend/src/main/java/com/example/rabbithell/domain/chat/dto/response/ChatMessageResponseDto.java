package com.example.rabbithell.domain.chat.dto.response;

import java.time.LocalDateTime;
import com.example.rabbithell.domain.chat.dto.MessageType;
import com.example.rabbithell.domain.chat.entity.ChatMessage;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatMessageResponseDto(
	MessageType messageType,
	String message,
	String username,

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdAt
) {
	@JsonCreator
	public ChatMessageResponseDto(
		@JsonProperty("messageType") MessageType messageType,
		@JsonProperty("message") String message,
		@JsonProperty("username") String username,
		@JsonProperty("createdAt") LocalDateTime createdAt
	) {
		this.messageType = messageType;
		this.message = message;
		this.username = username;
		this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
	}

	public static ChatMessageResponseDto createEnterMessage(String cloverName) {
		return new ChatMessageResponseDto(MessageType.ENTER, cloverName + "님이 입장하셨습니다.", cloverName, LocalDateTime.now());
	}

	public static ChatMessageResponseDto createChatMessage(String cloverName, ChatMessage chatMessage) {
		return new ChatMessageResponseDto(MessageType.CHAT, chatMessage.getContents(), cloverName, LocalDateTime.now());
	}

	public static ChatMessageResponseDto createQuitMessage(String cloverName) {
		return new ChatMessageResponseDto(MessageType.QUIT, cloverName + "님이 퇴장하셨습니다.", cloverName, LocalDateTime.now());
	}

	public static ChatMessageResponseDto createAdminMessage(Long id, String message) {
		return new ChatMessageResponseDto(MessageType.ADMIN, message, "관리자", LocalDateTime.now());
	}

	public static ChatMessageResponseDto system(long userCount, String message) {
		String sender = "현재 접속자 수: " + userCount + "명";
		return new ChatMessageResponseDto(MessageType.ADMIN, message, sender, LocalDateTime.now());
	}
}
