package com.example.rabbithell.domain.clover.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.auth.domain.MiniAuthUser;
import com.example.rabbithell.domain.clover.repository.CloverRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/oauth2/clover")
@RequiredArgsConstructor
public class CloverController {

	private final CloverRepository cloverRepository;

	@GetMapping("me")
	public ResponseEntity<CommonResponse<Map<String, Object>>> checkCloverExistence(
		@AuthenticationPrincipal MiniAuthUser authUser) {

		log.info("미니 인증 유저: {}", authUser);

		Long userId = authUser.getUserId();
		boolean hasClover = cloverRepository.existsByUserId(userId);

		Map<String, Object> result = new HashMap<>();
		result.put("hasClover", hasClover);

		return ResponseEntity.ok(CommonResponse.of(true, 200, "조회 성공", result));
	}

}
