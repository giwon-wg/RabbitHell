package com.example.rabbithell.domain.stigmasocket.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.stigmasocket.dto.request.CreateStigmaSocketRequest;
import com.example.rabbithell.domain.stigmasocket.dto.response.StigmaSocketResponse;
import com.example.rabbithell.domain.stigmasocket.service.StigmaSocketService;

@RestController
@RequiredArgsConstructor
public class StigmaSocketController {

	private final StigmaSocketService stigmaSocketService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<CommonResponse<StigmaSocketResponse>> creatStigmaSocket(
		@RequestBody @Valid CreateStigmaSocketRequest request
	) {
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"스티그마 소켓 생성 성공",
				stigmaSocketService.createStigmaSocket(request)));
	}
}
