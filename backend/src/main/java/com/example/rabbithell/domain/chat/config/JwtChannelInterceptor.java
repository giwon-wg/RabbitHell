package com.example.rabbithell.domain.chat.config;

import java.util.List;
import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.rabbithell.infrastructure.security.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

	private final JwtUtil jwtUtil;

	private static final String AUTHORIZATION = "Authorization";
	private static final String BEARER = "Bearer ";
	private static final String LOG_PREFIX = "[🔐 STOMP AUTH]";

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		// STOMP 메시지가 아니거나 accessor 파싱 실패 시 바로 반환
		if (accessor == null) {
			log.warn("{} ⚠️ STOMP 헤더 파싱 실패 - 메시지 무시", LOG_PREFIX);
			return message;
		}

		if (accessor.getCommand() != StompCommand.CONNECT) {
			return message;
		}

		log.info("{} 🚪 CONNECT 요청 처리 시작", LOG_PREFIX);

		try {
			Optional<String> tokenOptional = extractToken(accessor);

			if (tokenOptional.isEmpty()) {
				log.warn("{} ❌ JWT 토큰 없음", LOG_PREFIX);
				throw new IllegalArgumentException("JWT 토큰 누락 또는 형식 오류");
			}

			String token = tokenOptional.get();
			log.info("{} 📦 JWT 추출 완료: {}...", LOG_PREFIX, token.substring(0, Math.min(token.length(), 20)));

			if (!jwtUtil.validateToken(token)) {
				log.warn("{} ❌ 유효하지 않은 JWT", LOG_PREFIX);
				throw new IllegalArgumentException("JWT 유효성 검사 실패");
			}

			String username = jwtUtil.getUsernameFromToken(token);
			String role = jwtUtil.extractRole(token);

			List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
			Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
			accessor.setUser(authentication);
			accessor.getSessionAttributes().put("jwtToken", token);
			String roomId = accessor.getFirstNativeHeader("roomId");
			accessor.getSessionAttributes().put("roomId", roomId);

			log.info("{} ✅ 인증 성공: 사용자 = {}, 역할 = {}", LOG_PREFIX, username, role);
			return message;

		} catch (Exception e) {
			log.error("{} 💥 인증 실패: {}", LOG_PREFIX, e.getMessage(), e);
			throw e;
		}
	}

	private Optional<String> extractToken(StompHeaderAccessor accessor) {
		return Optional.ofNullable(accessor.getFirstNativeHeader(AUTHORIZATION))
			.filter(auth -> auth.startsWith(BEARER))
			.map(auth -> auth.substring(BEARER.length()))
			.or(() -> Optional.ofNullable(accessor.getFirstNativeHeader("authorization"))
				.filter(auth -> auth.startsWith(BEARER))
				.map(auth -> auth.substring(BEARER.length())))
			.or(() -> Optional.ofNullable(accessor.getFirstNativeHeader("token")))
			.or(() -> Optional.ofNullable(accessor.getFirstNativeHeader("X-Authorization"))
				.map(auth -> auth.startsWith(BEARER) ? auth.substring(BEARER.length()) : auth));
	}
}
