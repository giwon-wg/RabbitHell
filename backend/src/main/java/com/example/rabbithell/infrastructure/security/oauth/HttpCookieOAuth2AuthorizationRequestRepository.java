package com.example.rabbithell.infrastructure.security.oauth;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import com.example.rabbithell.config.CookieUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpCookieOAuth2AuthorizationRequestRepository
	implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

	private static final String COOKIE_NAME = "OAUTH2_AUTH_REQUEST";

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		return CookieUtils.getCookie(request, COOKIE_NAME)
			.map(cookie -> {
				try {
					return CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class);
				} catch (IllegalArgumentException | IllegalStateException e) {
					log.warn("쿠키 역직렬화 실패: {}", e.getMessage());
					return null;
				}
			})
			.orElse(null);
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
		HttpServletRequest request, HttpServletResponse response) {
		if (authorizationRequest == null) {
			CookieUtils.deleteCookie(request, response, COOKIE_NAME);
			return;
		}
		CookieUtils.addCookie(response, COOKIE_NAME,
			CookieUtils.serialize(authorizationRequest), 180);
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
		HttpServletResponse response) {
		OAuth2AuthorizationRequest authorizationRequest = loadAuthorizationRequest(request);
		CookieUtils.deleteCookie(request, response, COOKIE_NAME);
		return authorizationRequest;
	}
}
