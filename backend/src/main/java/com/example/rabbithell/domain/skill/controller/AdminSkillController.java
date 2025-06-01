package com.example.rabbithell.domain.skill.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.skill.dto.request.SkillCreateRequest;
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
			200,
			"스킬 생성 성공",
			skillId));
	}

}
