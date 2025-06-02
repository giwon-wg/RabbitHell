package com.example.rabbithell.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

public class CookieUtils {

	private static final ObjectMapper objectMapper = new ObjectMapper()
		.findAndRegisterModules()
		.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
		.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

	public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
		if (request.getCookies() == null) return Optional.empty();

		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals(name)) {
				return Optional.of(cookie);
			}
		}

		return Optional.empty();
	}

	public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setHttpOnly(false);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		if (request.getCookies() == null) return;

		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals(name)) {
				Cookie deleted = new Cookie(name, null);
				deleted.setPath("/");
				deleted.setMaxAge(0);
				response.addCookie(deleted);
			}
		}
	}

	public static String serialize(Object obj) {
		try {
			return Base64.getUrlEncoder().encodeToString(objectMapper.writeValueAsBytes(obj));
		} catch (Exception e) {
			throw new RuntimeException("쿠키 직렬화 실패", e);
		}
	}

	public static <T> T deserialize(Cookie cookie, Class<T> cls) {
		try {
			byte[] decoded = Base64.getUrlDecoder().decode(cookie.getValue());
			return objectMapper.readValue(decoded, cls);
		} catch (IOException e) {
			throw new RuntimeException("쿠키 역직렬화 실패", e);
		}
	}
}
