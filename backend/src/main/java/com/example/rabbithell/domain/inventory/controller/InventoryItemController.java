package com.example.rabbithell.domain.inventory.controller;

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
import com.example.rabbithell.domain.inventory.dto.response.InventoryItemResponse;
import com.example.rabbithell.domain.inventory.service.InventoryItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    @GetMapping
    public ResponseEntity<CommonResponse<PageResponse<InventoryItemResponse>>> getAllInventoryItems(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return ResponseEntity.ok(CommonResponse.of(
            true,
            HttpStatus.OK.value(),
            "인벤토리 아이템 전체 조회 성공",
            inventoryItemService.getAllInventoryItems(pageable)
        ));
    }

}
