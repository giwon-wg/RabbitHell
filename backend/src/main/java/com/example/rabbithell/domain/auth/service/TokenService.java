package com.example.rabbithell.domain.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;
import com.example.rabbithell.infrastructure.security.jwt.JwtUtil;
import com.example.rabbithell.domain.auth.dto.response.TokenResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

	private final CloverRepository cloverRepository;
	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;

	@Transactional
	public TokenResponse createFullToken(Long userId, String nickname, String cloverName) {

		User user = userRepository.findByIdOrElseThrow(userId);

		user.updateNickname(nickname);
		userRepository.save(user);

		Clover clover = Clover.builder()
			.user(user)
			.name(cloverName)
			.build();
		cloverRepository.save(clover);

		String accessToken = jwtUtil.generateAccessToken(user.getId().toString(), "USER", clover.getId(), clover.getName());
		String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString());

		return new TokenResponse(accessToken, refreshToken);
	}
}
