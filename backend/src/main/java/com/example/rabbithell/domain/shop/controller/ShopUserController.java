package com.example.rabbithell.domain.shop.controller;

import java.security.Principal;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.shop.dto.response.BuyItemResponse;
import com.example.rabbithell.domain.shop.dto.response.ShopItemResponse;
import com.example.rabbithell.domain.shop.dto.response.ShopResponse;
import com.example.rabbithell.domain.shop.service.ShopService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shops")
public class ShopUserController {

	private final ShopService shopService;

	@GetMapping("/{shopId}")
	public ResponseEntity<CommonResponse<ShopResponse>> getShop(@PathVariable Long shopId) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"상점 조회 성공",
			shopService.getShopById(shopId)
		));
	}

	@GetMapping("/{shopId}/items/{itemId}")
	public ResponseEntity<CommonResponse<ShopItemResponse>> getShopItem(@PathVariable Long itemId) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"상점 아이템 상세 조회 성공",
			shopService.getShopItem(itemId)
		));
	}

	@GetMapping("/{shopId}/items")
	public ResponseEntity<CommonResponse<PageResponse<ShopItemResponse>>> getAllShopItems(
		@PathVariable Long shopId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"상점 아이템 전체 조회 성공",
			shopService.getAllShopItems(shopId, pageable)
		));
	}

	@PostMapping("/{shopId}/items/{itemId}/buy")
	public ResponseEntity<CommonResponse<BuyItemResponse>> buyItem(
		@AuthenticationPrincipal AuthUser authUser,
		@PathVariable Long itemId,
		@RequestParam int quantity,
		Principal principal, UserDetails authenticatedPrincipal) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"상점 아이템 구매 성공",
			shopService.buyItem(authUser.getUserId(), itemId, quantity)
		));
	}

	@PostMapping("/{shopId}/sell")
	public ResponseEntity<CommonResponse<SellItemResponse>> sellItem(
		@PathVariable Long shopId,
		@RequestParam Long inventoryItemId
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"아이템 판매 성공",
			shopService.sellItem(shopId, inventoryItemId)
		));
	}

}
