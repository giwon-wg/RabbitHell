package com.example.rabbithell.domain.skill.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.skill.service.SkillService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/characters/{characterId}/skills")
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
public class UserSkillController {

	private final SkillService skillService;

	@PostMapping("/{skillId}/learn")
	public ResponseEntity<CommonResponse<Void>> learnSkill(
		@PathVariable Long characterId,
		@PathVariable Long skillId
	){
		skillService.learnSkill(characterId,skillId);
		return ResponseEntity.ok(
			CommonResponse.of(
				true,
				HttpStatus.OK.value(),
				"스킬 학습 성공"
			)
		);
	}

}
