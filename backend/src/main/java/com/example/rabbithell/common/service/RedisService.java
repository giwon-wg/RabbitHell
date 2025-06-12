package com.example.rabbithell.common.service;

import static com.example.rabbithell.common.exception.code.RedisExceptionCode.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Service;

import com.example.rabbithell.common.exception.RedisException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;

	public <T> void set(String key, T value) {
		try {
			String json = objectMapper.writeValueAsString(value);
			redisTemplate.opsForValue().set(key, json);
		} catch (RedisConnectionFailureException e) {
			throw new RedisException(REDIS_CONNECTION_FAIL);
		} catch (JsonProcessingException e) {
			throw new RedisException(REDIS_SERIALIZATION_FAIL);
		} catch (DataAccessException e) {
			throw new RedisException(REDIS_DATA_ACCESS_FAIL);
		}
	}

	public <T> T get(String key, Class<T> clazz) {
		try {
			String json = redisTemplate.opsForValue().get(key);
			if (json == null)
				return null;
			return objectMapper.readValue(json, clazz);
		} catch (RedisConnectionFailureException e) {
			throw new RedisException(REDIS_CONNECTION_FAIL);
		} catch (JsonProcessingException e) {
			throw new RedisException(REDIS_DESERIALIZATION_FAIL);
		} catch (DataAccessException e) {
			throw new RedisException(REDIS_DATA_ACCESS_FAIL);
		}
	}

	public <T> T get(String key, TypeReference<T> typeReference) {
		try {
			String json = redisTemplate.opsForValue().get(key);
			if (json == null)
				return null;
			return objectMapper.readValue(json, typeReference);
		} catch (RedisConnectionFailureException e) {
			throw new RedisException(REDIS_CONNECTION_FAIL);
		} catch (JsonProcessingException e) {
			throw new RedisException(REDIS_DESERIALIZATION_FAIL);
		} catch (DataAccessException e) {
			throw new RedisException(REDIS_DATA_ACCESS_FAIL);
		}
	}

	public void delete(String key) {
		try {
			redisTemplate.delete(key);
		} catch (RedisConnectionFailureException e) {
			throw new RedisException(REDIS_CONNECTION_FAIL);
		} catch (DataAccessException e) {
			throw new RedisException(REDIS_DATA_ACCESS_FAIL);
		}
	}
}
