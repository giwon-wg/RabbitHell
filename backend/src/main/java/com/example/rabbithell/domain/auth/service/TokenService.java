package com.example.rabbithell.domain.auth.service;

import com.example.rabbithell.domain.auth.dto.response.TokenResponse;

public interface TokenService {
	TokenResponse createFullToken(Long userId, String cloverName, Long kingdomId);
}
