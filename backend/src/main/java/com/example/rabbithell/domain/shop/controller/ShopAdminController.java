package com.example.rabbithell.domain.shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.shop.dto.request.AddItemRequest;
import com.example.rabbithell.domain.shop.dto.request.ShopRequest;
import com.example.rabbithell.domain.shop.dto.response.ShopItemResponse;
import com.example.rabbithell.domain.shop.dto.response.ShopResponse;
import com.example.rabbithell.domain.shop.service.ShopService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/shops")
@PreAuthorize("hasRole('ADMIN')")
public class ShopAdminController {

	private final ShopService shopService;

	@PostMapping
	public ResponseEntity<CommonResponse<ShopResponse>> createShop(
		@Valid @RequestBody ShopRequest shopRequest
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"상점 생성 성공",
			shopService.createShop(shopRequest)
		));
	}

	@PutMapping("/{shopId}")
	public ResponseEntity<CommonResponse<ShopResponse>> updateShop(
		@PathVariable Long shopId,
		@Valid @RequestBody ShopRequest shopRequest
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"상점 수정 성공",
			shopService.updateShop(shopId, shopRequest)
		));
	}

	@DeleteMapping("/{shopId}")
	public ResponseEntity<CommonResponse<Void>> deleteShop(
		@PathVariable Long shopId
	) {
		shopService.deleteShop(shopId);
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"상점 삭제 성공"
		));
	}

	@PostMapping("/{shopId}")
	public ResponseEntity<CommonResponse<ShopItemResponse>> addItem(
		@PathVariable Long shopId,
		@RequestBody AddItemRequest addItemRequest
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"상점에 아이템 추가 성공",
			shopService.addItem(shopId, addItemRequest)
		));
	}

}
