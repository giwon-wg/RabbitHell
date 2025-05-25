package com.example.rabbithell.auth.dto;

public record LoginResponse(
    String accessToken,
    String refreshToken
) {}
