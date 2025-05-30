package com.example.rabbithell.domain.item.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.dto.response.PageResponse;
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
public class ItemController {

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

	@GetMapping("/{itemId}")
	public ResponseEntity<CommonResponse<ItemResponse>> getPost(
		@PathVariable Long itemId
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"아이템 단건 조회 성공",
			itemService.getItemById(itemId)));
	}

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

	@PutMapping("/{itemId}")
	public ResponseEntity<CommonResponse<ItemResponse>> updatItem(
		@PathVariable Long itemId,
		@Valid @RequestBody ItemRequest itemRequest
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"아이템 수정 성공",
			itemService.updateItem(itemId, itemRequest)));
	}

	@DeleteMapping("/{itemId}")
	public ResponseEntity<CommonResponse<Void>> deleteItem(
		@PathVariable Long itemId
	) {
		itemService.deleteItem(itemId);
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"아이템 삭제 성공"));
	}

}
