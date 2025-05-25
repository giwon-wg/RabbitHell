package com.example.rabbithell.auth.dto;

public record TokenResponse(
    String accessToken,
    String refreshToken
) {}
