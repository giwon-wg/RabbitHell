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
import com.example.rabbithell.domain.skill.dto.request.PassiveSkillCreateRequest;
import com.example.rabbithell.domain.skill.dto.request.PassiveSkillUpdateRequest;
import com.example.rabbithell.domain.skill.dto.response.AllPassiveSkillResponse;
import com.example.rabbithell.domain.skill.service.PassiveSkillService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/skills/passive")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PassiveSkillController {

	private final PassiveSkillService passiveSkillService;

	// 패시브

	@PostMapping
	public ResponseEntity<CommonResponse<Long>> createPassiveSkill(
		@Valid @RequestBody PassiveSkillCreateRequest request
	) {
		Long skillId = passiveSkillService.createPassiveSkill(request);
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"스킬 생성 성공",
			skillId
			)
		);
	}

	@GetMapping
	public ResponseEntity<CommonResponse<Page<AllPassiveSkillResponse>>> getAllPassiveSkills(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String jobName
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<AllPassiveSkillResponse> skillsPage = passiveSkillService.getAllPassiveSkills(pageable, jobName);
		return ResponseEntity.ok(CommonResponse.of(
				true,
				HttpStatus.OK.value(),
				"패시브 스킬 목록 조회 성공",
				skillsPage
			)
		);
	}

	@PatchMapping("/{skillId}")
	public ResponseEntity<CommonResponse<Void>> updatePassiveSkill(
		@PathVariable Long skillId,
		@RequestBody @Valid PassiveSkillUpdateRequest request
	) {
		passiveSkillService.updatePassiveSkill(skillId, request);
		return ResponseEntity.ok(CommonResponse.of(
				true,
				HttpStatus.OK.value(),
				"패시브 스킬 수정 성공"
			)
		);
	}

	@DeleteMapping("/{skillId}")
	public ResponseEntity<CommonResponse<Void>> deletePassiveSkill(@PathVariable Long skillId) {
		passiveSkillService.deletePassiveSkill(skillId);
		return ResponseEntity.ok(CommonResponse.of(
				true,
				HttpStatus.OK.value(),
				"스킬 삭제 성공"
			)
		);
	}
}
