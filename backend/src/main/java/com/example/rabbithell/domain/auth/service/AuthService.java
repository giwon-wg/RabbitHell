package com.example.rabbithell.domain.auth.service;

import static com.example.rabbithell.domain.auth.exception.code.AuthExceptionCode.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.auth.dto.request.SignupRequest;
import com.example.rabbithell.domain.auth.dto.response.LoginResponse;
import com.example.rabbithell.domain.auth.dto.response.TokenResponse;
import com.example.rabbithell.domain.auth.exception.AuthException;
import com.example.rabbithell.domain.expedition.entity.Expedition;
import com.example.rabbithell.domain.expedition.repository.ExpeditionRepository;
import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.repository.InventoryRepository;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;
import com.example.rabbithell.infrastructure.security.jwt.JwtUtil;
import com.example.rabbithell.infrastructure.security.persistence.RedisRefreshTokenAdapter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RedisRefreshTokenAdapter redisRefreshTokenAdapter;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
	private final ExpeditionRepository expeditionRepository;

	@Transactional
    public void signup(SignupRequest request) {

        if (userRepository.findByEmailAndIsDeletedFalse(request.email()).isPresent()) {
            throw new AuthException(DUPLICATED_EMAIL);
        }

		if (expeditionRepository.existsByName(request.expeditionName())) {
			throw new AuthException(DUPLICATED_EXPEDITION_NAME);
		}

        User user = User.builder()
            .email(request.email())
            .name(request.name())
            .password(passwordEncoder.encode(request.password()))
            .role(User.Role.valueOf(request.role().toUpperCase()))
            .isDeleted(false)
            .build();

        User savedUser = userRepository.save(user);

		Expedition expedition = new Expedition(request.expeditionName(), savedUser);
		expeditionRepository.save(expedition);

        Inventory inventory = Inventory.builder()
            .user(savedUser)
            .capacity(100)
            .build();

        inventoryRepository.save(inventory);
    }

	@Transactional
    public LoginResponse login(String email, String rawPassword) {

		User user = userRepository.findByEmailOrElseThrow(email);

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new AuthException(INVALID_PASSWORD);
        }

		Expedition expedition = expeditionRepository.findByUserIdOrElseThrow(user.getId());

        String accessToken = jwtUtil.generateAccessToken(user.getId().toString(), user.getRole().name(), expedition.getId(), expedition.getName());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString());

        redisRefreshTokenAdapter.save(user.getId(), refreshToken);

        return new LoginResponse(accessToken, refreshToken);
    }

	@Transactional
    public TokenResponse reissue(String refreshToken) {

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new AuthException(REFRESH_TOKEN_MISMATCH);
        }

        Long userId = Long.parseLong(jwtUtil.extractSubject(refreshToken));

		userRepository.findByIdOrElseThrow(userId);

        String saved = redisRefreshTokenAdapter.getByUserId(userId)
            .orElseThrow(() -> new AuthException(REFRESH_TOKEN_NOT_FOUND));

        if (!refreshToken.equals(saved)) {
            throw new AuthException(REFRESH_TOKEN_MISMATCH);
        }

		Expedition expedition = expeditionRepository.findByUserIdOrElseThrow(userId);

        String newAccessToken = jwtUtil.generateAccessToken(userId.toString(), jwtUtil.extractRole(refreshToken), expedition.getId(), expedition.getName());
        String newRefreshToken = jwtUtil.generateRefreshToken(userId.toString());

        redisRefreshTokenAdapter.save(userId, newRefreshToken);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

	@Transactional
    public void logout(Long userId) {

		userRepository.findByIdOrElseThrow(userId);

        redisRefreshTokenAdapter.delete(userId);
    }

}
