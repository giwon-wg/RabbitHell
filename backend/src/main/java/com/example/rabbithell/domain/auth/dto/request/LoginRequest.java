package com.example.rabbithell.domain.auth.dto.request;

public record LoginRequest(
	String email,
	String password
) {
}
