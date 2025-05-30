package com.example.rabbithell.domain.stigma.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import lombok.RequiredArgsConstructor;

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
import com.example.rabbithell.domain.stigma.dto.request.CreateStigmaRequest;
import com.example.rabbithell.domain.stigma.dto.request.StigmaCond;
import com.example.rabbithell.domain.stigma.dto.request.UpdateStigmaRequest;
import com.example.rabbithell.domain.stigma.dto.response.StigmaResponse;
import com.example.rabbithell.domain.stigma.service.StigmaService;

@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/stigmas")
public class StigmaController {

	private final StigmaService stigmaService;


	@PostMapping()
	public ResponseEntity<CommonResponse<StigmaResponse>> createStigma(
		@RequestBody @Valid CreateStigmaRequest request
	) {
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"스티그마 생성 성공",
				stigmaService.createStigma(request)));
	}

	@GetMapping("/page/")
	@Validated
	public ResponseEntity<CommonResponse<PageResponse<StigmaResponse>>> findAllStigmaByCond(
		@RequestParam(defaultValue = "1") @Min(1) int page,
        @RequestParam(defaultValue = "10") @Min(1) int size,
		@ModelAttribute StigmaCond cond
	) {

        return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"스티그마 전체 조회 성공",
                stigmaService.findAllStigmaByCond(page, size, cond)));
	}

	@GetMapping("/{stigmaId}")
	public ResponseEntity<CommonResponse<StigmaResponse>> findStigmaById (
		@PathVariable Long stigmaId
	) {
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"스티그마 개별 조회 성공",
				stigmaService.findStigmaById(stigmaId)));
	}

	@PatchMapping("/{stigmaId}")
	public ResponseEntity<CommonResponse<Void>> updateStigma(
		@PathVariable Long stigmaId,
		@RequestBody @Valid UpdateStigmaRequest request
	) {
		stigmaService.updateStigma(stigmaId, request);
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"스티그마 수정 성공"));
	}

	@DeleteMapping("/{stigmaId}")
	public ResponseEntity<CommonResponse<Void>> deleteStigma(
		@PathVariable Long stigmaId
	) {
		stigmaService.deleteStigma(stigmaId);
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"스티그마 삭제 성공"));
	}
}
