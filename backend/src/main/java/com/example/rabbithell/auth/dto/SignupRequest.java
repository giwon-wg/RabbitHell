package com.example.rabbithell.auth.dto;

public record SignupRequest(
    String email,
    String password,
    String role
) {}
