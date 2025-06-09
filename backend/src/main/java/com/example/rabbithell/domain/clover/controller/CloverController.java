package com.example.rabbithell.domain.clover.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.auth.domain.MiniAuthUser;
import com.example.rabbithell.domain.clover.dto.response.CloverNameResponse;
import com.example.rabbithell.domain.clover.dto.response.CloverPublicResponse;
import com.example.rabbithell.domain.clover.dto.response.CloverResponse;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.clover.service.CloverService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class CloverController {

	private final CloverService cloverService;

	@GetMapping("/oauth2/clover/me")
	public ResponseEntity<CommonResponse<Map<String, Object>>> checkCloverExistence(
		@AuthenticationPrincipal MiniAuthUser authUser) {

		Map<String, Object> result = cloverService.getCloverInfoForMiniToken(authUser.getUserId());

		return ResponseEntity.ok(CommonResponse.of(
			true,
			200,
			"조회 성공",
			result));
	}


	@GetMapping("/clover/me")
	public ResponseEntity<CommonResponse<CloverResponse>> getMyClover(
		@AuthenticationPrincipal AuthUser authUser) {
		return ResponseEntity.ok(CommonResponse.of(true,
			200,
			"조회 성공",
			cloverService.getMyClover(authUser.getUserId())));
	}

	@GetMapping("/clover/{cloverId}")
	public ResponseEntity<CommonResponse<CloverPublicResponse>> getPublicClover(
		@PathVariable Long cloverId) {
		return ResponseEntity.ok(CommonResponse.of(true,
			200,
			"조회 성공",
			cloverService.getCloverById(cloverId)));
	}

	@GetMapping("/clover/names")
	public ResponseEntity<CommonResponse<List<CloverNameResponse>>> getAllCloverNames() {
		List<CloverNameResponse> response = cloverService.getAllCloverNames();
		return ResponseEntity.ok(CommonResponse.of(true,
			200,
			"조회 성공",
			response));
	}


}
