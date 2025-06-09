package com.example.rabbithell.infrastructure.security.jwt;

import java.util.List;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtTokenExtractor {

	//클라이언트가 어디에 토큰을 넣었든지(헤더, 세션, 쿼리등) 그걸 꺼내기만 하는 용도
	//JWT 토큰을 여러 방법으로 추출 시도
	public String extractToken(SimpMessageHeaderAccessor accessor) {
		// 1. 세션 속성에서 토큰 추출
		String token = (String) accessor.getSessionAttributes().get("jwtToken");
		if (StringUtils.hasText(token)) {
			return token;
		}

		// 2. 헤더에서 Authorization 토큰 추출
		token = (String) accessor.getSessionAttributes().get("Authorization");
		if (StringUtils.hasText(token)) {
			return token.startsWith("Bearer ") ? token.substring(7) : token;
		}

		// 3. native headers에서 추출
		Object authHeader = accessor.getNativeHeader("Authorization");
		if (authHeader instanceof java.util.List) {
			@SuppressWarnings("unchecked")
			java.util.List<String> authHeaders = (java.util.List<String>) authHeader;
			if (!authHeaders.isEmpty()) {
				String headerValue = authHeaders.get(0);
				return headerValue.startsWith("Bearer ") ? headerValue.substring(7) : headerValue;
			}
		}

		// 4. 쿼리 파라미터에서 토큰 추출 (fallback)
		token = (String) accessor.getSessionAttributes().get("token");
		if (StringUtils.hasText(token)) {
			return token;
		}

		return null;
	}
}
