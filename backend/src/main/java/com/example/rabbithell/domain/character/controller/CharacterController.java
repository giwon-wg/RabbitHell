package com.example.rabbithell.domain.character.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.domain.character.dto.request.CreateCharacterRequest;
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
    public ResponseEntity<String> createCharacter(
        @Valid @RequestBody CreateCharacterRequest request,
        @AuthenticationPrincipal User user
    ){
        characterService.createCharacter(user,request);
        return ResponseEntity.status(HttpStatus.CREATED).body("캐릭터 생성이 완료되었습니다.");
    }

}
