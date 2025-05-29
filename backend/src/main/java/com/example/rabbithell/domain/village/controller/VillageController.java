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
import com.example.rabbithell.domain.village.dto.request.MoveCharacterRequest;
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
    ){

        villageService.moveVillage(authUser, request.characterId(), request.targetVillageId());

        return ResponseEntity.ok(CommonResponse.of(true, HttpStatus.OK.value(), "마을 이동 성공" ));

    }
}
