package com.example.rabbithell.domain.chat.config;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
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

import com.example.rabbithell.domain.chat.exception.ChatMessageException;
import com.example.rabbithell.domain.chat.exception.ChatMessageExceptionCode;
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
	private static final String LOG_PREFIX = "[STOMP AUTH]";

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		if (accessor == null) {
			log.warn("{} STOMP 헤더 파싱 실패 - 메시지 무시", LOG_PREFIX);
			return message;
		}

		if (accessor.getCommand() != StompCommand.CONNECT) {
			return message;
		}

		log.info("{} CONNECT 요청 처리 시작", LOG_PREFIX);

		try {
			// 1. JWT 추출
			Optional<String> tokenOptional = extractToken(accessor);

			if (tokenOptional.isEmpty()) {
				log.warn("{} ❌ JWT 토큰 없음", LOG_PREFIX);
				throw new ChatMessageException(ChatMessageExceptionCode.UNAUTHORIZED);
			}

			String token = tokenOptional.get();
			log.info("{} JWT 추출 완료: {}...", LOG_PREFIX, token.substring(0, Math.min(token.length(), 20)));

			// 2. JWT 유효성 검사
			if (!jwtUtil.validateToken(token)) {
				log.warn("{} ❌ 유효하지 않은 JWT", LOG_PREFIX);
				throw new ChatMessageException(ChatMessageExceptionCode.UNAUTHORIZED);
			}

			// 3. 사용자 정보 추출 및 인증 객체 생성
			String username = jwtUtil.getUsernameFromToken(token);
			String role = jwtUtil.extractRole(token);
			Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));

			List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
			Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
			accessor.setUser(authentication);

			// 4. roomId는 NativeHeader에서 추출
			String roomId = accessor.getFirstNativeHeader("roomId");
			if (roomId == null || roomId.isBlank()) {
				log.warn("{} ❌ roomId 누락", LOG_PREFIX);
				throw new ChatMessageException(ChatMessageExceptionCode.ROOM_NUMBER_ERROR);
			}

			// 5. 세션에 사용자 정보 저장
			accessor.getSessionAttributes().put("jwtToken", token);
			accessor.getSessionAttributes().put("userId", userId);
			accessor.getSessionAttributes().put("roomId", roomId);

			log.info("{} ✅ 인증 성공: 사용자 = {}, 역할 = {}, ID = {}, 방번호 = {}", LOG_PREFIX, username, role, userId, roomId);

			return message;

		} catch (ChatMessageException e) {
			log.error("{} 인증 실패: {}", LOG_PREFIX, e.getMessage());
			throw e;

		} catch (Exception e) {
			log.error("{} 예기치 않은 인증 오류 발생: {}", LOG_PREFIX, e.getMessage(), e);
			throw new ChatMessageException(ChatMessageExceptionCode.UNAUTHORIZED);
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
