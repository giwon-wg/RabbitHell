package com.example.rabbithell.domain.auth.service;

import static com.example.rabbithell.domain.clover.exception.code.CloverExceptionCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.exception.CloverException;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;
import com.example.rabbithell.infrastructure.security.jwt.JwtUtil;
import com.example.rabbithell.domain.auth.dto.response.TokenResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

	private final CloverRepository cloverRepository;
	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;

	@Transactional
	public TokenResponse createFullToken(Long userId, String cloverName) {

		User user = userRepository.findByIdOrElseThrow(userId);
		// 사이드 임펙트 방지
		userRepository.save(user);

		Clover clover = cloverRepository.findByUserId(userId).orElse(null);
		if (clover == null) {
			clover = Clover.builder()
				.user(user)
				.name(cloverName)
				.cash(1000L)
				.saving(1000L)
				.build();
			cloverRepository.save(clover);
		}

		String accessToken = jwtUtil.generateAccessToken(user.getId().toString(), "USER", clover.getId(), clover.getName());
		String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString());

		return new TokenResponse(accessToken, refreshToken);
	}
}
