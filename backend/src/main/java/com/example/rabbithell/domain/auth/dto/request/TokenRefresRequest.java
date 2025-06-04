package com.example.rabbithell.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenRefresRequest(
	@JsonProperty("refreshToken")
	String refreshToken
) {
}
