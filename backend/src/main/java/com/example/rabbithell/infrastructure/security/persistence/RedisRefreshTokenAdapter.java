package com.example.rabbithell.infrastructure.security.persistence;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisRefreshTokenAdapter {

    private static final String KEY_PREFIX = "refresh-token:";

    private final RedisTemplate<String, String> redisTemplate;

    public void save(Long userId, String refreshToken) {
        String key = generateKey(userId);
        redisTemplate.opsForValue().set(key, refreshToken);
    }

    public Optional<String> getByUserId(Long userId) {
        String key = generateKey(userId);
        String token = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(token);
    }

    public void delete(Long userId) {
        String key = generateKey(userId);
        redisTemplate.delete(key);
    }

    private String generateKey(Long userId) {
        return KEY_PREFIX + userId;
    }
}
