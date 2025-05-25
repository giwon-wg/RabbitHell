package com.example.rabbithell.auth.dto;

public record LoginRequest(
    String email,
    String password
) {}
