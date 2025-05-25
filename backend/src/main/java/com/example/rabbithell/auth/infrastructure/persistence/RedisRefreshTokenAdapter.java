package com.example.rabbithell.auth.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.example.rabbithell.auth.port.out.RefreshTokenPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisRefreshTokenAdapter implements RefreshTokenPort {

    private static final String KEY_PREFIX = "refresh-token:";

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String userId, String refreshToken) {
        String key = generateKey(userId);
        redisTemplate.opsForValue().set(key, refreshToken);
    }

    @Override
    public Optional<String> getByUserId(String userId) {
        String key = generateKey(userId);
        String token = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(token);
    }

    @Override
    public void delete(String userId) {
        String key = generateKey(userId);
        redisTemplate.delete(key);
    }

    private String generateKey(String userId) {
        return KEY_PREFIX + userId;
    }
}
