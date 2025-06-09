package com.example.rabbithell.domain.characterSkill.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.characterSkill.dto.response.EquippedSkillResponse;
import com.example.rabbithell.domain.characterSkill.dto.response.LearnedSkillResponse;
import com.example.rabbithell.domain.characterSkill.service.CharacterSkillService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/characters/{characterId}/skills")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class CharacterSkillController {

	private final CharacterSkillService characterSkillService;

	@PostMapping("/{skillId}/learn")
	public ResponseEntity<CommonResponse<Void>> learnSkill(
		@PathVariable Long characterId,
		@PathVariable Long skillId
	) {
		characterSkillService.learnSkill(characterId, skillId);
		return ResponseEntity.ok(
			CommonResponse.of(
				true,
				200,
				"스킬 학습 성공"
			)
		);
	}

	@PostMapping("/{skillId}/equip")
	public ResponseEntity<CommonResponse<Void>> equipSkill(
		@PathVariable Long characterId,
		@PathVariable Long skillId) {
		characterSkillService.equipSkill(characterId, skillId);
		return ResponseEntity.ok(
			CommonResponse.of(
				true,
				200,
				"스킬 장착 성공"
			)
		);
	}

	@PostMapping("/{skillId}/unequip")
	public ResponseEntity<CommonResponse<Void>> unequipSkill(
		@PathVariable Long characterId,
		@PathVariable Long skillId
	) {
		characterSkillService.unequipSkill(characterId, skillId);
		return ResponseEntity.ok(
			CommonResponse.of(
				true,
				200,
				"스킬 해제 완료"
			)
		);
	}

	@GetMapping("/learned")
	public ResponseEntity<CommonResponse<Page<LearnedSkillResponse>>> getLearnedSkills(
		@PathVariable Long characterId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<LearnedSkillResponse> learnedSkills = characterSkillService.getLearnedSkills(characterId, pageable);
		return ResponseEntity.ok(
			CommonResponse.of(
				true,
				200,
				"배운 스킬 목록 조회 성공",
				learnedSkills
			)
		);
	}

	@GetMapping("/learnable")
	public ResponseEntity<CommonResponse<Page<LearnedSkillResponse>>> getLearnableSkills(
		@PathVariable Long characterId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<LearnedSkillResponse> learnableSkills = characterSkillService.getLearnableSkills(characterId, pageable);
		return ResponseEntity.ok(
			CommonResponse.of(
				true,
				200,
				"습득 가능한 스킬 목록 조회 성공",
				learnableSkills
			)
		);
	}

	@GetMapping("/equipped")
	public ResponseEntity<CommonResponse<List<EquippedSkillResponse>>> getEquippedSkills(@PathVariable Long characterId) {
		List<EquippedSkillResponse> equippedSkills = characterSkillService.getEquippedSkills(characterId);
		return ResponseEntity.ok(
			CommonResponse.of(
				true,
				200,
				"장착된 스킬 목록 조회 성공",
				equippedSkills
			)
		);
	}

}
