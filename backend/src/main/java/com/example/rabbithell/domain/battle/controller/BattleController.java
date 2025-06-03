package com.example.rabbithell.domain.battle.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.battle.dto.request.DoBattleRequest;
import com.example.rabbithell.domain.battle.dto.request.GetBattleFieldsRequest;
import com.example.rabbithell.domain.battle.dto.response.BattleResultResponse;
import com.example.rabbithell.domain.battle.dto.response.GetBattleFieldsResponse;
import com.example.rabbithell.domain.battle.service.BattleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/battles")
@RequiredArgsConstructor
public class BattleController {

	private final BattleService battleService;

	@GetMapping
	public ResponseEntity<CommonResponse<GetBattleFieldsResponse>> getAvailableMaps(
		@AuthenticationPrincipal AuthUser authUser,
		@Valid @RequestBody GetBattleFieldsRequest request
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"전투 필드 조회 완료",
			battleService.getBattleFields(authUser, request.characterId())
		));
	}

	@PatchMapping
	public ResponseEntity<CommonResponse<BattleResultResponse>> doBattle(
		@AuthenticationPrincipal AuthUser authUser,
		@Valid @RequestBody DoBattleRequest request
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"전투 완료",
			battleService.doBattle(authUser, request.battleFieldType())
		));
	}
}
