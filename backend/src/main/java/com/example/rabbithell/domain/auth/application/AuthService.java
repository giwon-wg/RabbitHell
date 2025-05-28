package com.example.rabbithell.domain.auth.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;
import com.example.rabbithell.domain.auth.dto.response.LoginResponse;
import com.example.rabbithell.domain.auth.dto.request.SignupRequest;
import com.example.rabbithell.domain.auth.dto.response.TokenResponse;
import com.example.rabbithell.infrastructure.security.jwt.JwtUtil;
import com.example.rabbithell.infrastructure.security.persistence.RedisRefreshTokenAdapter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RedisRefreshTokenAdapter redisRefreshTokenAdapter;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signup(SignupRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        User user = User.builder()
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .role(User.Role.valueOf(request.role().toUpperCase()))
            .build();

        userRepository.save(user);
    }

    public LoginResponse login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId().toString(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString());

        redisRefreshTokenAdapter.save(user.getId().toString(), refreshToken);

        return new LoginResponse(accessToken, refreshToken);
    }

    public TokenResponse reissue(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰");
        }

        String userId = jwtUtil.extractSubject(refreshToken);
        String saved = redisRefreshTokenAdapter.getByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰 없음"));

        if (!refreshToken.equals(saved)) {
            throw new IllegalArgumentException("리프레시 토큰 불일치");
        }

        String newAccessToken = jwtUtil.generateAccessToken(userId, jwtUtil.extractRole(refreshToken));
        String newRefreshToken = jwtUtil.generateRefreshToken(userId);

        redisRefreshTokenAdapter.save(userId, newRefreshToken);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    public void logout(String userId) {
        redisRefreshTokenAdapter.delete(userId);
    }

}
