package com.example.rabbithell.infrastructure.security.oauth.handler;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.rabbithell.infrastructure.security.jwt.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtUtil jwtUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {

		try {
			String userId = authentication.getName();
			String role = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

			String miniToken = jwtUtil.createMiniToken(userId, role);
			String redirectUrl = "http://localhost:3000/oauth/success?miniToken=" + miniToken;

			response.sendRedirect(redirectUrl);
		} catch (NoSuchElementException e) {
		log.warn("OAuth2 로그인: 사용자 권한 없음", e);
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "권한 정보가 없음");
		} catch (IllegalArgumentException e) {
		log.warn("OAuth2 로그인: 잘못된 토큰 생성 파라미터", e);
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 로그인 정보");
		} catch (IOException e) {
		log.error("리다이렉트 중 I/O 오류 발생", e);
		throw e;
		} catch (Exception e) {
		log.error("OAuth2 Success 핸들러 중 알 수 없는 오류 발생", e);
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "OAuth2 로그인 처리 중 오류 발생");
		}
	}
}
