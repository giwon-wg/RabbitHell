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
import com.example.rabbithell.domain.characterSkill.dto.response.EquippedActiveSkillResponse;
import com.example.rabbithell.domain.characterSkill.dto.response.LearnableActiveSkillResponse;
import com.example.rabbithell.domain.characterSkill.dto.response.LearnedActiveSkillResponse;
import com.example.rabbithell.domain.characterSkill.enums.SkillEquipType;
import com.example.rabbithell.domain.characterSkill.service.CharacterActiveSkillService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/characters/{characterId}/skills/active")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class CharacterActiveSkillController {

	private final CharacterActiveSkillService characterActiveSkillService;

	@PostMapping("/{skillId}/learn")
	public ResponseEntity<CommonResponse<Void>> learnSkill(
		@PathVariable Long characterId,
		@PathVariable Long skillId
	) {
		characterActiveSkillService.learnActiveSkill(characterId, skillId);
		return ResponseEntity.ok(
			CommonResponse.of(
				true,
				200,
				"액티브 스킬 학습 성공"
			)
		);
	}

	@PostMapping("/{skillId}/equip")
	public ResponseEntity<CommonResponse<Void>> equipSkill(
		@PathVariable Long characterId,
		@PathVariable Long skillId,
		@RequestParam SkillEquipType slotType) {
		characterActiveSkillService.equipActiveSkill(characterId, skillId, slotType);
		return ResponseEntity.ok(
			CommonResponse.of(
				true,
				200,
				"액티브 스킬 장착 성공"
			)
		);
	}

	@PostMapping("/{skillId}/unequip")
	public ResponseEntity<CommonResponse<Void>> unequipSkill(
		@PathVariable Long characterId,
		@PathVariable Long skillId
	) {
		characterActiveSkillService.unequipActiveSkill(characterId, skillId);
		return ResponseEntity.ok(
			CommonResponse.of(
				true,
				200,
				"액티브 스킬 해제 완료"
			)
		);
	}

	@GetMapping("/learned")
	public ResponseEntity<CommonResponse<Page<LearnedActiveSkillResponse>>> getLearnedSkills(
		@PathVariable Long characterId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<LearnedActiveSkillResponse> learnedSkills = characterActiveSkillService.getLearnedActiveSkills(characterId,
			pageable);
		return ResponseEntity.ok(
			CommonResponse.of(
				true,
				200,
				"배운 액티브 스킬 목록 조회 성공",
				learnedSkills
			)
		);
	}

	@GetMapping("/learnable")
	public ResponseEntity<CommonResponse<Page<LearnableActiveSkillResponse>>> getLearnableSkills(
		@PathVariable Long characterId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<LearnableActiveSkillResponse> learnableSkills = characterActiveSkillService.getLearnableActiveSkills(
			characterId, pageable);
		return ResponseEntity.ok(
			CommonResponse.of(
				true,
				200,
				"습득 가능한 액티브 스킬 목록 조회 성공",
				learnableSkills
			)
		);
	}

	@GetMapping("/equipped")
	public ResponseEntity<CommonResponse<List<EquippedActiveSkillResponse>>> getEquippedSkills(
		@PathVariable Long characterId) {
		List<EquippedActiveSkillResponse> equippedSkills = characterActiveSkillService.getEquippedActiveSkills(
			characterId);
		return ResponseEntity.ok(
			CommonResponse.of(
				true,
				200,
				"장착된 액티브 스킬 목록 조회 성공",
				equippedSkills
			)
		);
	}

}
