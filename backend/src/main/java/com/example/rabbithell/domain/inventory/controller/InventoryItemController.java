package com.example.rabbithell.domain.inventory.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.inventory.dto.request.EquipRequest;
import com.example.rabbithell.domain.inventory.dto.request.UseRequest;
import com.example.rabbithell.domain.inventory.dto.response.EquipResponse;
import com.example.rabbithell.domain.inventory.dto.response.EquipableItemResponse;
import com.example.rabbithell.domain.inventory.dto.response.InventoryItemResponse;
import com.example.rabbithell.domain.inventory.dto.response.UnequipResponse;
import com.example.rabbithell.domain.inventory.dto.response.UseResponse;
import com.example.rabbithell.domain.inventory.enums.Slot;
import com.example.rabbithell.domain.inventory.service.InventoryItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory/inventory-items")
public class InventoryItemController {

	private final InventoryItemService inventoryItemService;

	@GetMapping("/{inventoryItemId}")
	public ResponseEntity<CommonResponse<InventoryItemResponse>> getInventoryItem(
		@AuthenticationPrincipal AuthUser authUser,
		@PathVariable Long inventoryItemId
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"인벤토리 아이템 단건 조회 성공",
			inventoryItemService.getInventoryItemById(authUser.getUserId(), inventoryItemId)
		));
	}

	@GetMapping
	public ResponseEntity<CommonResponse<PageResponse<InventoryItemResponse>>> getAllInventoryItemsBySlot(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) Slot slot
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"(슬롯 별) 인벤토리 아이템 전체 조회 성공",
			inventoryItemService.getAllInventoryItemsFilterBySlot(authUser.getUserId(), slot, pageable)
		));
	}

	@GetMapping("/equipable")
	public ResponseEntity<CommonResponse<PageResponse<EquipableItemResponse>>> getAllEquipableInventoryItemsBySlot(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) Slot slot
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"(슬롯 별) 장착 가능한 인벤토리 아이템 전체 조회 성공",
			inventoryItemService.getAllEquipableInventoryItems(authUser.getUserId(), slot, pageable)
		));
	}

	@GetMapping("/equipped")
	public ResponseEntity<CommonResponse<EquipResponse>> getEquippedItemsByCharacter(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestParam Long characterId
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"캐릭터 장착 아이템 조회 성공",
			inventoryItemService.getEquippedItemsByCharacter(authUser.getUserId(), characterId)
		));
	}

	@PostMapping("/{inventoryItemId}/equip")
	public ResponseEntity<CommonResponse<EquipResponse>> equipItem(
		@AuthenticationPrincipal AuthUser authUser,
		@PathVariable Long inventoryItemId,
		@RequestParam Long characterId
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"인벤토리 아이템 장착 성공",
			inventoryItemService.equipItem(authUser.getUserId(), inventoryItemId, characterId)
		));
	}

	@PostMapping("/{inventoryItemId}/unequip")
	public ResponseEntity<CommonResponse<UnequipResponse>> unequipItem(
		@AuthenticationPrincipal AuthUser authUser,
		@PathVariable Long inventoryItemId
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"인벤토리 아이템 장착 해제 성공",
			inventoryItemService.unequipItem(authUser.getUserId(), inventoryItemId)
		));
	}

	@PostMapping("/{inventoryItemId}/use")
	public ResponseEntity<CommonResponse<UseResponse>> useItem(
		@AuthenticationPrincipal AuthUser authUser,
		@PathVariable Long inventoryItemId,
		@RequestBody UseRequest useRequest
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"인벤토리 아이템 사용 성공",
			inventoryItemService.useItem(authUser.getUserId(), inventoryItemId, useRequest)
		));
	}

	@DeleteMapping("/{inventoryItemId}/discard")
	public ResponseEntity<CommonResponse<Void>> discardItem(
		@AuthenticationPrincipal AuthUser authUser,
		@PathVariable Long inventoryItemId
	) {
		inventoryItemService.discardItem(authUser.getUserId(), inventoryItemId);

		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"인벤토리 아이템 버리기 성공"
		));
	}

}
