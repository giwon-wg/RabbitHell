package com.example.rabbithell.domain.stigma.controller;

import java.util.List;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.stigma.dto.request.CreateStigmaRequest;
import com.example.rabbithell.domain.stigma.dto.request.UpdateStigmaRequest;
import com.example.rabbithell.domain.stigma.dto.response.StigamResponse;
import com.example.rabbithell.domain.stigma.service.StigmaService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/stigmas")
public class StigmaController {

	private final StigmaService stigmaService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping()
	public ResponseEntity<CommonResponse<StigamResponse>> create(
		@RequestBody @Valid CreateStigmaRequest request
	) {
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"스티그마 생성 성공",
				stigmaService.create(request)));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/page/{pageNumber}")
	public ResponseEntity<CommonResponse<List<StigamResponse>>> findAll(
@PathVariable int pageNumber, @RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"스티그마 전체 조회 성공",
				stigmaService.findAll(pageNumber, size)));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{stigmaId}")
	public ResponseEntity<CommonResponse<StigamResponse>> findById (
		@PathVariable Long stigmaId
	) {
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"스티그마 개별 조회 성공",
				stigmaService.findById(stigmaId)));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/{stigmaId}")
	public ResponseEntity<CommonResponse<Void>> update(
		@PathVariable Long stigmaId,
		@RequestBody @Valid UpdateStigmaRequest request
	) {
		stigmaService.update(stigmaId, request);
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"스티그마 수정 성공"));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{stigmaId}")
	public ResponseEntity<CommonResponse<Void>> delete(
		@PathVariable Long stigmaId
	) {
		stigmaService.delete(stigmaId);
		return ResponseEntity.ok(
			CommonResponse.of(true,
				HttpStatus.OK.value(),
				"스티그마 삭제 성공"));
	}

}
