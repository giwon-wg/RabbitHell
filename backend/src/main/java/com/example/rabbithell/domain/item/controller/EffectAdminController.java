package com.example.rabbithell.domain.item.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.community.post.dto.response.PostResponse;
import com.example.rabbithell.domain.item.dto.request.EffectRequest;
import com.example.rabbithell.domain.item.dto.response.EffectResponse;
import com.example.rabbithell.domain.item.service.EffectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/effects")
@RequiredArgsConstructor
public class EffectAdminController {

	private final EffectService effectService;

	@PostMapping
	public ResponseEntity<CommonResponse<EffectResponse>> createEffect(
		@Valid @RequestBody EffectRequest effectRequest
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"특수 효과 생성 성공",
			effectService.createEffect(effectRequest)
		));
	}

	@GetMapping("/{effectId}")
	public ResponseEntity<CommonResponse<EffectResponse>> getEffect(
		@PathVariable Long effectId
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"특수 효과 단건 조회 성공",
			effectService.getEffectById(effectId)));
	}

	@PutMapping("/{effectId}")
	public ResponseEntity<CommonResponse<EffectResponse>> updateEffect(
		@PathVariable Long effectId,
		@Valid @RequestBody EffectRequest effectRequest
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"특수 효과 수정 성공",
			effectService.updateEffect(effectId, effectRequest)));
	}

}
