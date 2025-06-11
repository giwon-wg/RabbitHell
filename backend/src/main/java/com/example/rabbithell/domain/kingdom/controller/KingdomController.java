package com.example.rabbithell.domain.kingdom.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.kingdom.dto.response.KingdomResponse;
import com.example.rabbithell.domain.kingdom.service.KingdomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/kingdom")
@RequiredArgsConstructor
public class KingdomController {

	private final KingdomService kingdomService;

	@GetMapping("/{id}")
	public ResponseEntity<CommonResponse<KingdomResponse>> findByKingdom(
		@PathVariable Long id
	) {
		return ResponseEntity.ok(
			CommonResponse.of(
				true,
				HttpStatus.OK.value(),
				"왕국 조회 성공",
				kingdomService.findKingdomById(id))
		);
	}
}
