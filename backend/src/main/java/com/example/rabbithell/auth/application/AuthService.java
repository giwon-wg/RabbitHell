package com.example.rabbithell.auth.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.rabbithell.auth.port.out.JwtTokenPort;
import com.example.rabbithell.auth.port.out.RefreshTokenPort;
import com.example.rabbithell.user.domain.model.User;
import com.example.rabbithell.user.domain.repository.UserRepository;
import com.example.rabbithell.auth.dto.LoginResponse;
import com.example.rabbithell.auth.dto.SignupRequest;
import com.example.rabbithell.auth.dto.TokenResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenPort jwtTokenPort;
    private final RefreshTokenPort refreshTokenPort;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        String accessToken = jwtTokenPort.generateAccessToken(user.getId().toString(), user.getRole().name());
        String refreshToken = jwtTokenPort.generateRefreshToken(user.getId().toString());

        refreshTokenPort.save(user.getId().toString(), refreshToken);

        return new LoginResponse(accessToken, refreshToken);
    }

    public TokenResponse reissue(String refreshToken) {
        if (!jwtTokenPort.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰");
        }

        String userId = jwtTokenPort.extractSubject(refreshToken);
        String saved = refreshTokenPort.getByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰 없음"));

        if (!refreshToken.equals(saved)) {
            throw new IllegalArgumentException("리프레시 토큰 불일치");
        }

        String newAccessToken = jwtTokenPort.generateAccessToken(userId, jwtTokenPort.extractRole(refreshToken));
        String newRefreshToken = jwtTokenPort.generateRefreshToken(userId);

        refreshTokenPort.save(userId, newRefreshToken);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    public void logout(String userId) {
        refreshTokenPort.delete(userId);
    }

}
