package com.example.rabbithell.domain.inventory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.inventory.dto.response.EquipResponse;
import com.example.rabbithell.domain.inventory.dto.response.InventoryResponse;
import com.example.rabbithell.domain.inventory.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

	private final InventoryService inventoryService;

	@PostMapping("/expand")
	public ResponseEntity<CommonResponse<InventoryResponse>> expandInventory(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestParam int amount
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"인벤토리 확장 성공",
			inventoryService.expandInventory(authUser.getUserId(), amount)
		));
	}

}
