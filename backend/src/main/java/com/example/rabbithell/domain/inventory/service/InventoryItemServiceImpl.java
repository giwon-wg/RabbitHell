package com.example.rabbithell.domain.inventory.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.inventory.dto.response.InventoryItemResponse;
import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.entity.InventoryItemId;
import com.example.rabbithell.domain.inventory.repository.InventoryItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryItemServiceImpl implements InventoryItemService {

	private final InventoryItemRepository inventoryItemRepository;

	@Transactional(readOnly = true)
	@Override
	public InventoryItemResponse getInventoryItemById(Long userId, Long itemId) {
		InventoryItemId id = new InventoryItemId(userId, itemId);
		InventoryItem inventoryItem = inventoryItemRepository.findByIdOrElseThrow(id);
		return InventoryItemResponse.fromEntity(inventoryItem);
	}

	@Transactional(readOnly = true)
	@Override
	public PageResponse<InventoryItemResponse> getAllInventoryItems(Pageable pageable) {
		Page<InventoryItem> page = inventoryItemRepository.findAll(pageable);

		List<InventoryItemResponse> dtoList = page.getContent().stream()
			.map(InventoryItemResponse::fromEntity)
			.toList();

		return PageResponse.of(dtoList, page);
	}

}
