package com.example.rabbithell.domain.chat.dto.request;


public record ChatMessageDeleteRequestDto(
	Long messageId,
	String reason
) {}
