package com.example.rabbithell.domain.chat.config;

import com.example.rabbithell.infrastructure.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Arrays;
import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

	private final JwtUtil jwtUtil;

	public JwtHandshakeInterceptor(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
								   WebSocketHandler wsHandler, Map<String, Object> attributes) {
		String query = request.getURI().getQuery();
		System.out.println("Handshake query: " + query);

		if (query == null || !query.contains("token=")) {
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			return false;
		}

		String token = Arrays.stream(query.split("&"))
			.filter(param -> param.startsWith("token="))
			.map(param -> param.substring(6))
			.findFirst()
			.orElse(null);

		System.out.println("Handshake token: " + token);

		if (!jwtUtil.validateToken(token)) {
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			return false;
		}

		// ✅ 유저 정보 추출 및 저장
		Claims claims = jwtUtil.parseClaims(token);
		String userId = claims.getSubject();
		String username = claims.get("cloverName", String.class);

		attributes.put("jwtToken", token); // ✅ 반드시 추가
		attributes.put("userId", userId);
		attributes.put("username", username);

		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
							   WebSocketHandler wsHandler, Exception exception) {
		// 필요 시 핸드셰이크 이후 처리
	}
}
