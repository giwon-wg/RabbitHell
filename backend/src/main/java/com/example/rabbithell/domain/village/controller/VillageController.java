package com.example.rabbithell.domain.village.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.village.dto.request.MoneyRequest;
import com.example.rabbithell.domain.village.dto.request.MoveCharacterRequest;
import com.example.rabbithell.domain.village.dto.request.UserRequest;
import com.example.rabbithell.domain.village.service.VillageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/villages")
@RequiredArgsConstructor
public class VillageController {
	private final VillageService villageService;

	@PatchMapping("/move")
	public ResponseEntity<CommonResponse<Void>> moveVillage(
		@AuthenticationPrincipal AuthUser authUser,
		@Valid @RequestBody MoveCharacterRequest request
	) {

		villageService.moveVillage(authUser, request.targetVillageId());

		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"마을 이동 성공"
		));

	}

	@PatchMapping("/banks/save")
	public ResponseEntity<CommonResponse<Void>> saveMoney(
		@AuthenticationPrincipal AuthUser authUser,
		@Valid @RequestBody MoneyRequest request
	) {
		villageService.saveMoney(authUser, request.money());

		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			request.money() + "골드가 입금되었습니다."
		));
	}

	@PatchMapping("/banks/withdraw")
	public ResponseEntity<CommonResponse<Void>> withdrawMoney(
		@AuthenticationPrincipal AuthUser authUser,
		@Valid @RequestBody MoneyRequest request
	) {
		villageService.withdrawMoney(authUser, request.money());

		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			request.money() + "골드가 출금되었습니다."));
	}

	@PatchMapping("/hospitals/cure")
	public ResponseEntity<CommonResponse<Void>> cureCharacter(
		@AuthenticationPrincipal AuthUser authUser,
		@Valid @RequestBody UserRequest request
	) {
		villageService.cureCharacter(authUser, request.characterId());

		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"치료가 완료되었습니다."
		));
	}

}
