package com.example.rabbithell.domain.auth.dto.request;

public record SignupRequest(
    String email,
    String password,
    String role
) {}
