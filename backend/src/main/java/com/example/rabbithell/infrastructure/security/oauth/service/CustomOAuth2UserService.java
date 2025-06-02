package com.example.rabbithell.infrastructure.security.oauth.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

		OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(request);

		Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");

		String email = (String) kakaoAccount.get("email");

		User user = userRepository.findByEmailAndIsDeletedFalse(email)
			.orElseGet(() -> userRepository.saveAndFlush(
				User.builder()
				.name("TEMP")
				.email(email)
				.password("KAKAO")
				.role(User.Role.USER)
				.isDeleted(false)
				.build()));


		return new DefaultOAuth2User(
			List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
			Map.of("userId", user.getId().toString()),
			"userId"
		);
	}
}
