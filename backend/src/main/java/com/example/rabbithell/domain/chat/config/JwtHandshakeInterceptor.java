package com.example.rabbithell.domain.chat.config;

import com.example.rabbithell.infrastructure.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Arrays;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {


	private final JwtUtil jwtUtil;

	public JwtHandshakeInterceptor(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
								   WebSocketHandler wsHandler, Map<String, Object> attributes) {

		if (request instanceof ServletServerHttpRequest servletRequest) {
			HttpServletRequest req = servletRequest.getServletRequest();
			String token = req.getParameter("token");

			if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
				// 사용자 정보 추출해서 세션에 저장
				String username = jwtUtil.getUsernameFromToken(token);
				attributes.put("jwtToken", token);
				attributes.put("username", username); // 선택 사항
				return true;
			}
		}

		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		return false;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
							   WebSocketHandler wsHandler, Exception exception) {
	}
}
