package com.example.rabbithell.domain.auth.dto.response;

public record LoginResponse(String accessToken, String refreshToken) {
}
