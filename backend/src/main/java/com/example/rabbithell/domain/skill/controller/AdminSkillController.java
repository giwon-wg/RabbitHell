package com.example.rabbithell.domain.skill.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.skill.dto.request.SkillCreateRequest;
import com.example.rabbithell.domain.skill.dto.request.SkillUpdateRequest;
import com.example.rabbithell.domain.skill.dto.response.AllSkillResponse;
import com.example.rabbithell.domain.skill.service.SkillService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/skills")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminSkillController {

	private final SkillService skillService;

	@PostMapping
	public ResponseEntity<CommonResponse<Long>> createSkill(
		@Valid @RequestBody SkillCreateRequest request
	) {
		Long skillId = skillService.createSkill(request);
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"스킬 생성 성공",
			skillId));
	}

	@GetMapping
	public ResponseEntity<CommonResponse<Page<AllSkillResponse>>> getAllSkills(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String jobName
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<AllSkillResponse> skillsPage = skillService.getAllSkills(pageable, jobName);
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"스킬 목록 조회 성공",
			skillsPage
			)
		);
	}

	@PatchMapping("/{skillId}")
	public ResponseEntity<CommonResponse<Void>> updateSkill(
		@PathVariable Long skillId,
		@RequestBody @Valid SkillUpdateRequest request
	) {
		skillService.updateSkill(skillId, request);
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"스킬 수정 성공",
			null
			)
		);
	}

	@DeleteMapping("/{skillId}")
	public ResponseEntity<CommonResponse<Void>> deleteSkill(@PathVariable Long skillId) {
		skillService.deleteSkill(skillId);
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"스킬 삭제 성공",
			null
			)
		);
	}

}
