package com.example.rabbithell.domain.deck.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.deck.dto.request.CreateDeckRequest;
import com.example.rabbithell.domain.deck.dto.response.DeckResponse;
import com.example.rabbithell.domain.deck.service.AdminDeckService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminDeckController {

	private final AdminDeckService adminDeckService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/admin/clovers/{cloverId}/decks")
	public ResponseEntity<CommonResponse<DeckResponse>> createDeck(
		@PathVariable Long cloverId,
		@RequestBody @Valid CreateDeckRequest request
	) {
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"덱 생성 성공",
				adminDeckService.createDeck(cloverId, request)));
	}
}
