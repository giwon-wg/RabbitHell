package com.example.rabbithell.domain.chat.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChatMessageRequestDto(
	@NotBlank(message = "내용은 반드시 작성되어야 합니다.") String message
) {
}
