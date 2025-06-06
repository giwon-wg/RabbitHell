package com.example.rabbithell.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;

import com.example.rabbithell.infrastructure.security.jwt.JwtAuthenticationFilter;
import com.example.rabbithell.infrastructure.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.rabbithell.infrastructure.security.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.example.rabbithell.infrastructure.security.oauth.service.CustomOAuth2UserService;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
	private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.cors(Customizer.withDefaults())
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(sess ->
				sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.exceptionHandling(ex ->
				ex.authenticationEntryPoint((request, response, authException) -> {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				})
			)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/auth/**",
					"/oauth2/authorization/**",
					"/swagger-ui/**",
					"/v3/api-docs/**",
					"/ws/**",         // ✅ 반드시 필요
					"/ws/**/**"
				).permitAll()
				.anyRequest().authenticated()
			)
			.oauth2Login(oauth -> oauth
				.authorizationEndpoint(auth ->
					auth.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
				)
				.userInfoEndpoint(userInfo ->
					userInfo.userService(customOAuth2UserService)
				)
				.successHandler(oAuth2AuthenticationSuccessHandler)
				.failureHandler((req, res, ex) -> {
					Cookie[] cookies = req.getCookies();
					if (cookies != null && Arrays.stream(cookies)
						.anyMatch(cookie -> cookie.getName().equals("OAUTH2_FAILED"))) {
						res.setStatus(429);
						return;
					}

					Cookie failFlag = new Cookie("OAUTH2_FAILED", "true");
					failFlag.setMaxAge(60);
					failFlag.setPath("/");
					res.addCookie(failFlag);

					httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequest(req, res);
					res.sendRedirect("http://localhost:8080/login-failed");
				})
			)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}


