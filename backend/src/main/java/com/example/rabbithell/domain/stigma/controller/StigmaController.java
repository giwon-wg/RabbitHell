package com.example.rabbithell.domain.stigma.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.stigma.dto.request.CreateStigmaRequest;
import com.example.rabbithell.domain.stigma.dto.response.CreateStigamResponse;
import com.example.rabbithell.domain.stigma.service.StigmaService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/stigmas")
public class StigmaController {

    private final StigmaService stigmaService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<CommonResponse<CreateStigamResponse>> create(
        @RequestBody @Valid CreateStigmaRequest request,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        return ResponseEntity.ok(
            CommonResponse.of(true,
                HttpStatus.OK.value(),
                "스티그마 생성 성공",
                stigmaService.create(request)));
    }
}
