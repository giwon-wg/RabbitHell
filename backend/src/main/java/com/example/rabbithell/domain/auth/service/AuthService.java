package com.example.rabbithell.domain.auth.service;

import com.example.rabbithell.domain.auth.dto.request.SignupRequest;
import com.example.rabbithell.domain.auth.dto.request.TokenRefresRequest;
import com.example.rabbithell.domain.auth.dto.response.LoginResponse;
import com.example.rabbithell.domain.auth.dto.response.TokenResponse;

public interface AuthService {

	void signup(SignupRequest request);

	LoginResponse login(String email, String rawPassword);

	TokenResponse reissue(TokenRefresRequest tokenRefresRequest);

	void logout(Long userId);
}
