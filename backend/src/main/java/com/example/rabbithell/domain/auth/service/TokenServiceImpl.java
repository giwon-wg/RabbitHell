package com.example.rabbithell.domain.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.service.CloverServiceImpl;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;
import com.example.rabbithell.infrastructure.security.jwt.JwtUtil;
import com.example.rabbithell.domain.auth.dto.response.TokenResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;
	private final CloverServiceImpl cloverService;

	@Override
	@Transactional
	public TokenResponse createFullToken(Long userId, String cloverName, Long kingdomId) {

		User user = userRepository.findByIdOrElseThrow(userId);

		Clover clover = cloverService.createClover(user, cloverName, userId, kingdomId);

		String accessToken = jwtUtil.generateAccessToken(user.getId().toString(), "USER", clover.getId(), clover.getName());
		String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString());

		return new TokenResponse(accessToken, refreshToken);
	}
}
