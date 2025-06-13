package com.example.rabbithell.domain.skill.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.example.rabbithell.domain.skill.dto.request.ActiveSkillCreateRequest;
import com.example.rabbithell.domain.skill.dto.request.ActiveSkillUpdateRequest;
import com.example.rabbithell.domain.skill.dto.response.AllActiveSkillResponse;
import com.example.rabbithell.domain.skill.service.ActiveSkillService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/skills/active")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ActiveSkillController {

	private final ActiveSkillService activeSkillService;

	// 액티브
	@PostMapping
	public ResponseEntity<CommonResponse<Long>> createActiveSkill(
		@Valid @RequestBody ActiveSkillCreateRequest request
	) {
		Long skillId = activeSkillService.createActiveSkill(request);
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"스킬 생성 성공",
			skillId));
	}

	@GetMapping
	public ResponseEntity<CommonResponse<Page<AllActiveSkillResponse>>> getAllActiveSkills(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String jobName
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<AllActiveSkillResponse> skillsPage = activeSkillService.getAllActiveSkills(pageable, jobName);
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"스킬 목록 조회 성공",
			skillsPage
			)
		);
	}

	@PatchMapping("/{skillId}")
	public ResponseEntity<CommonResponse<Void>> updateActiveSkill(
		@PathVariable Long skillId,
		@RequestBody @Valid ActiveSkillUpdateRequest request
	) {
		activeSkillService.updateActiveSkill(skillId, request);
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"스킬 수정 성공",
			null
			)
		);
	}

	@DeleteMapping("/{skillId}")
	public ResponseEntity<CommonResponse<Void>> deleteActiveSkill(
		@PathVariable Long skillId
	) {
		activeSkillService.deleteActiveSkill(skillId);
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"스킬 삭제 성공",
			null
			)
		);
	}

}
