package com.example.rabbithell.domain.auth.service;

import static com.example.rabbithell.domain.auth.exception.code.AuthExceptionCode.*;
import static com.example.rabbithell.domain.clover.exception.code.CloverExceptionCode.DUPLICATED_CLOVER_NAME;

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
import com.example.rabbithell.domain.deck.entity.EffectDetail;
import com.example.rabbithell.domain.deck.entity.PawCardEffect;
import com.example.rabbithell.domain.deck.enums.EffectDetailSlot;
import com.example.rabbithell.domain.deck.repository.PawCardEffectRepository;
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
	private final InventoryRepository inventoryRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final CloverRepository cloverRepository;
	private final PawCardEffectRepository pawCardEffectRepository;

	@Transactional
	public void signup(SignupRequest request) {

		if (userRepository.findByEmailAndIsDeletedFalse(request.email()).isPresent()) {
			throw new AuthException(DUPLICATED_EMAIL);
		}

		if (cloverRepository.existsByName(request.name())) {
			throw new CloverException(DUPLICATED_CLOVER_NAME);
		}

		User user = User.builder()
			.email(request.email())
			.name(request.name())
			.password(passwordEncoder.encode(request.password()))
			.role(User.Role.valueOf(request.role().toUpperCase()))
			.isDeleted(false)
			.build();
		User savedUser = userRepository.save(user);

		Clover clover = new Clover(request.cloverName(), savedUser);
		Clover savedClover = cloverRepository.save(clover);

		Inventory inventory = Inventory.builder()
			.clover(savedClover)
			.capacity(100)
			.build();

		inventoryRepository.save(inventory);

		PawCardEffect pawCardEffect = PawCardEffect.builder().clover(clover).build();
		EffectDetail effectDetail1 = EffectDetail.builder()
			.effectDetailSlot(EffectDetailSlot.EFFECT_DETAIL_SLOT_1)
			.pawCardEffect(pawCardEffect)
			.build();
		EffectDetail effectDetail2 = EffectDetail.builder()
			.effectDetailSlot(EffectDetailSlot.EFFECT_DETAIL_SLOT_2)
			.pawCardEffect(pawCardEffect)
			.build();

		pawCardEffect.addEffectDetail(effectDetail1);
		pawCardEffect.addEffectDetail(effectDetail2);

		pawCardEffectRepository.save(pawCardEffect);
	}

	@Transactional
	public LoginResponse login(String email, String rawPassword) {

		User user = userRepository.findByEmailOrElseThrow(email);

		if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
			throw new AuthException(INVALID_PASSWORD);
		}

		Clover clover = cloverRepository.findByUserIdOrElseThrow(user.getId());

		String accessToken = jwtUtil.generateAccessToken(user.getId().toString(), user.getRole().name(), clover.getId(),
			clover.getName());
		String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString());

		redisRefreshTokenAdapter.save(user.getId(), refreshToken);

		return new LoginResponse(accessToken, refreshToken);
	}

	@Transactional
	public TokenResponse reissue(TokenRefresRequest tokenRefresRequest) {

		String refreshToken = tokenRefresRequest.refreshToken();

		log.info("리프레쉬 토큰 값: " + refreshToken);

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

		String newAccessToken = jwtUtil.generateAccessToken(userId.toString(), jwtUtil.extractRole(refreshToken),
			clover.getId(), clover.getName());
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
