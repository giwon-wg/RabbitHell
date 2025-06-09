package com.example.rabbithell.domain.pawcard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.pawcard.dto.request.CreatePawCardRequest;
import com.example.rabbithell.domain.pawcard.dto.request.PawCardCond;
import com.example.rabbithell.domain.pawcard.dto.request.UpdatePawCardRequest;
import com.example.rabbithell.domain.pawcard.dto.response.PawCardResponse;
import com.example.rabbithell.domain.pawcard.service.PawCardService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/paw-cards")
public class PawCardController {

	private final PawCardService pawCardService;

	@PostMapping()
	public ResponseEntity<CommonResponse<PawCardResponse>> createPawCard(
		@RequestBody @Valid CreatePawCardRequest request
	) {
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"포카드 생성 성공",
				pawCardService.createPawCard(request)));
	}

	@GetMapping("/page/")
	@Validated
	public ResponseEntity<CommonResponse<PageResponse<PawCardResponse>>> findAllPawCardByCond(
		@RequestParam(defaultValue = "1") @Min(1) int page,
		@RequestParam(defaultValue = "10") @Min(1) int size,
		@ModelAttribute PawCardCond cond
	) {

		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"포카드 전체 조회 성공",
				pawCardService.findAllPawCardByCond(page, size, cond)));
	}

	@GetMapping("/{pawCardId}")
	public ResponseEntity<CommonResponse<PawCardResponse>> findPawCardById(
		@PathVariable Long pawCardId
	) {
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"포카드 개별 조회 성공",
				pawCardService.findPawCardById(pawCardId)));
	}

	@PatchMapping("/{pawCardId}")
	public ResponseEntity<CommonResponse<Void>> updatePawCard(
		@PathVariable Long pawCardId,
		@RequestBody @Valid UpdatePawCardRequest request
	) {
		pawCardService.updatePawCard(pawCardId, request);
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"포카드 수정 성공"));
	}

	@DeleteMapping("/{pawCardId}")
	public ResponseEntity<CommonResponse<Void>> deletePawCard(
		@PathVariable Long pawCardId
	) {
		pawCardService.deletePawCard(pawCardId);
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"포카드 삭제 성공"));
	}
}
