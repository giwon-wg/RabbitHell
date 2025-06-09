package com.example.rabbithell.domain.auth.service;

import static com.example.rabbithell.domain.auth.exception.code.AuthExceptionCode.*;
import static com.example.rabbithell.domain.clover.exception.code.CloverExceptionCode.DUPLICATED_CLOVER_NAME;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.auth.dto.request.SignupRequest;
import com.example.rabbithell.domain.auth.dto.request.TokenRefresRequest;
import com.example.rabbithell.domain.auth.dto.response.LoginResponse;
import com.example.rabbithell.domain.auth.dto.response.TokenResponse;
import com.example.rabbithell.domain.auth.exception.AuthException;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.exception.CloverException;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.repository.InventoryRepository;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;
import com.example.rabbithell.infrastructure.security.jwt.JwtUtil;
import com.example.rabbithell.infrastructure.security.persistence.RedisRefreshTokenAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final RedisRefreshTokenAdapter redisRefreshTokenAdapter;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
	private final CloverRepository cloverRepository;

	@Value("${ADMIN_KEY}")
	private String adminKey;

	@Transactional
    public void signup(SignupRequest request) {

        if (userRepository.findByEmailAndIsDeletedFalse(request.email()).isPresent()) {
            throw new AuthException(DUPLICATED_EMAIL);
        }

		if (cloverRepository.existsByName(request.name())) {
			throw new CloverException(DUPLICATED_CLOVER_NAME);
		}

		validateAdminKey(request.key(), adminKey);

        User user = User.builder()
            .email(request.email())
            .name(request.name())
            .password(passwordEncoder.encode(request.password()))
            .role(User.Role.valueOf("ADMIN"))
            .isDeleted(false)
            .build();
        userRepository.save(user);

    }

	@Transactional
    public LoginResponse login(String email, String rawPassword) {

		User user = userRepository.findByEmailOrElseThrow(email);

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new AuthException(INVALID_PASSWORD);
        }

		String accessToken = jwtUtil.createMiniToken(user.getId(), user.getRole().name());
		String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString());

        redisRefreshTokenAdapter.save(user.getId(), refreshToken);

        return new LoginResponse(accessToken, refreshToken);
    }

	@Transactional
    public TokenResponse reissue(TokenRefresRequest tokenRefresRequest) {

		String refreshToken = tokenRefresRequest.refreshToken();

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new AuthException(INVALID_REFRESH_TOKEN);
        }

        Long userId = Long.parseLong(jwtUtil.extractSubject(refreshToken));

		userRepository.findByIdOrElseThrow(userId);

        String saved = redisRefreshTokenAdapter.getByUserId(userId)
            .orElseThrow(() -> new AuthException(REFRESH_TOKEN_NOT_FOUND));

        if (!refreshToken.equals(saved)) {
            throw new AuthException(REFRESH_TOKEN_MISMATCH);
        }

		Clover clover = cloverRepository.findByUserIdOrElseThrow(userId);

        String newAccessToken = jwtUtil.generateAccessToken(userId.toString(), jwtUtil.extractRole(refreshToken), clover.getId(), clover.getName());
        String newRefreshToken = jwtUtil.generateRefreshToken(userId.toString());

        redisRefreshTokenAdapter.save(userId, newRefreshToken);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

	@Transactional
    public void logout(Long userId) {

		userRepository.findByIdOrElseThrow(userId);

        redisRefreshTokenAdapter.delete(userId);
    }

	public static void validateAdminKey(String inputKey, String actualKey) {
		if (!actualKey.equals(inputKey)) {
			throw new AuthException(ADMIN_KEY_MISMATCH);
		}
	}
}
