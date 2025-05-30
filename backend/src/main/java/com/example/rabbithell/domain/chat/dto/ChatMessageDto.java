package com.example.rabbithell.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ChatMessageDto {

	private final Long chatUserId;
	private final MessageType messageType;

	@NotBlank(message = "내용은 반드시 작성되어야 합니다.")
	private final String message;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime createdAt;

	@JsonCreator
	public ChatMessageDto(@JsonProperty("chatUserId") Long chatUserId,
						  @JsonProperty("messageType") MessageType messageType,
						  @JsonProperty("message") String message) {
		this.chatUserId = chatUserId;
		this.messageType = messageType;
		this.message = message;
		this.createdAt = LocalDateTime.now();
	}
}
