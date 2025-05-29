package com.example.rabbithell.domain.item.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.item.dto.request.ItemRequest;
import com.example.rabbithell.domain.item.dto.response.ItemResponse;
import com.example.rabbithell.domain.item.service.ItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/items")
@PreAuthorize("hasRole('ADMIN')")
public class ItemAdminController {

    // cud

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<CommonResponse<ItemResponse>> createItem(
        @Valid @RequestBody ItemRequest itemRequest
    ) {
        return ResponseEntity.ok(CommonResponse.of(
            true,
            HttpStatus.OK.value(),
            "아이템 생성 성공",
            itemService.createItem(itemRequest)
        ));
    }

}
