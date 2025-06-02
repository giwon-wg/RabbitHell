package com.example.rabbithell.domain.auth.dto.request;

public record SignupRequest(
	String email,
	String name,
	String password,
	String role,
	String cloverName
) {
}
