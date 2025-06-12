package com.example.rabbithell.domain.deck.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.deck.dto.request.BatchActivePawCardRequest;
import com.example.rabbithell.domain.deck.dto.request.DeckCond;
import com.example.rabbithell.domain.deck.dto.response.BatchActivePawCardResponse;
import com.example.rabbithell.domain.deck.dto.response.DeckResponse;
import com.example.rabbithell.domain.deck.service.DeckService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/decks")
@RequiredArgsConstructor
public class DeckController {

	private final DeckService deckService;

	@GetMapping
	public ResponseEntity<CommonResponse<PageResponse<DeckResponse>>> findAllDeckByCond(
		@RequestParam(defaultValue = "1") @Min(1) int page,
		@RequestParam(defaultValue = "13") @Min(1) int size,
		@ModelAttribute DeckCond cond,
		@AuthenticationPrincipal AuthUser authUser
	) {
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"덱 전체 조회 성공",
				deckService.findAllDeckByCond(authUser.getCloverId(), page, size, cond))
		);
	}

	@GetMapping("/{deckId}")
	public ResponseEntity<CommonResponse<DeckResponse>> findDeckById(
		@PathVariable Long deckId,
		@AuthenticationPrincipal AuthUser authUser
	) {
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"덱 개별 조회 성공",
				deckService.findDeckById(deckId, authUser.getCloverId())));
	}

	@PostMapping("/calculate-effect")
	public ResponseEntity<CommonResponse<Void>> calculateEffect(
		@AuthenticationPrincipal AuthUser authUser
	) {
		deckService.calculateEffect(authUser.getCloverId());
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"PawCard 효과 계산 성공")
		);
	}

	@PostMapping("/assign-slots")
	public ResponseEntity<CommonResponse<Void>> assignSlots(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody @Valid BatchActivePawCardRequest request
	) {
		deckService.assignSlots(authUser.getCloverId(), request);
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"PawCard 변경 성공")
		);
	}

	@GetMapping("/final-response")
	public ResponseEntity<CommonResponse<BatchActivePawCardResponse>> getFinalResponse(
		@AuthenticationPrincipal AuthUser authUser
	) {
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"PawCard다!! 뀨!!!",
				deckService.getFinalResponse(authUser.getCloverId()))
		);
	}
}
