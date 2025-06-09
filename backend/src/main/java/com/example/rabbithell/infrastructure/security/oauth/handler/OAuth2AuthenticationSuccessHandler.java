package com.example.rabbithell.infrastructure.security.oauth.handler;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.rabbithell.infrastructure.security.jwt.JwtUtil;

import jakarta.servlet.ServletException;
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
		Authentication authentication) throws IOException, ServletException {

		try {
			String userId = authentication.getName();
			String role = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

			log.info("userId={}, role={}", userId, role);

			String miniToken = jwtUtil.createMiniToken(Long.parseLong(userId), role);
			String redirectUrl = "http://localhost:3000/oauth/success?miniToken=" + miniToken;

			response.sendRedirect(redirectUrl);
		} catch (Exception e) {
			log.error("OAuth2 Success 핸들러 중 오류 발생", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "OAuth2 로그인 처리 중 오류 발생");
		}

		// String userId = authentication.getName();
		//
		// Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		// String role = authorities.iterator().next().getAuthority().replace("ROLE_", "");
		//
		// String miniToken = jwtUtil.createMiniToken(Long.parseLong(userId), role);
		//
		// response.setContentType("application/json");
		// response.setCharacterEncoding("UTF-8");
		//
		// String body = """
        //     {
        //         "success": true,
        //         "message": "카카오 로그인 성공",
        //         "miniToken": "%s"
        //     }
        //     """.formatted(miniToken);
		//
		// response.getWriter().write(body);
	}
}
