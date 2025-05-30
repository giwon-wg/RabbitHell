package com.example.rabbithell.domain.auth.dto.response;

public record TokenResponse(
	String accessToken,
	String refreshToken
) {
}
