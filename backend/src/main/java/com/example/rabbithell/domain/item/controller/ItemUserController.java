package com.example.rabbithell.domain.item.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.item.dto.response.ItemResponse;
import com.example.rabbithell.domain.item.service.ItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory/items")
public class ItemUserController {

    private final ItemService itemService;

    // 아이템 목록 조회
    @GetMapping
    public ResponseEntity<CommonResponse<PageResponse<ItemResponse>>> getAllItems(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return ResponseEntity.ok(CommonResponse.of(
            true,
            HttpStatus.OK.value(),
            "아이템 전체 조회 성공",
            itemService.getAllItems(pageable)
        ));
    }

}
