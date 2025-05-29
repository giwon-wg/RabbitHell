package com.example.rabbithell.domain.character.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.character.dto.request.CreateCharacterRequest;
import com.example.rabbithell.domain.character.dto.response.CharacterInfoResponse;
import com.example.rabbithell.domain.character.service.CharacterService;
import com.example.rabbithell.domain.user.model.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/characters")
    public ResponseEntity<CommonResponse<Long>> createCharacter(
        @Valid @RequestBody CreateCharacterRequest request,
        @AuthenticationPrincipal User user
    ){
        Long characterId = characterService.createCharacter(user,request);
        return ResponseEntity.ok(
            CommonResponse.of(
            true,
            HttpStatus.OK.value(),
            "캐릭터 생성 성공",
            characterId
            )
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/characters/{characterId}")
    public ResponseEntity<CommonResponse<CharacterInfoResponse>> characterInfo(
        @PathVariable Long characterId,
        @AuthenticationPrincipal User user
    ){
        return ResponseEntity.ok(
            CommonResponse.of(
            true,
            HttpStatus.OK.value(),
            "캐릭터 조회 성공",
            characterService.characterInfo(characterId, user)
            )
        );
    }
}
