package com.example.rabbithell.domain.auth.dto.request;

public record FullJwtRequest(
	String nickname,
	String cloverName
) {
}
