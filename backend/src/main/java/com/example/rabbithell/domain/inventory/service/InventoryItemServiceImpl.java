package com.example.rabbithell.domain.inventory.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.inventory.dto.request.EquipRequest;
import com.example.rabbithell.domain.inventory.dto.response.EquipResponse;
import com.example.rabbithell.domain.inventory.dto.response.InventoryItemResponse;
import com.example.rabbithell.domain.inventory.dto.response.UnequipResponse;
import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.repository.InventoryItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryItemServiceImpl implements InventoryItemService {

	private final InventoryItemRepository inventoryItemRepository;
	private final CharacterRepository characterRepository;

	@Transactional(readOnly = true)
	@Override
	public InventoryItemResponse getInventoryItemById(Long userId, Long inventoryItemId) {
		InventoryItem inventoryItem = inventoryItemRepository.findByIdAndValidateOwner(inventoryItemId, userId);

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

	@Transactional
	@Override
	public EquipResponse equipItem(Long userId, Long inventoryItemId, EquipRequest equipRequest) {
		// 인벤토리 아이템 조회
		InventoryItem inventoryItem = inventoryItemRepository.findByIdAndValidateOwner(inventoryItemId, userId);

		// 아이템을 장착하기 위해 캐릭터 조회
		Long characterId = equipRequest.characterId();
		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);

		// 아이템 장착
		inventoryItem.equip(character, equipRequest.slot());

		// 응답은 캐릭터가 장착 중인 모든 아이템
		return inventoryItemRepository.findEquipmentStatusByCharacterId(characterId);
	}

	@Transactional
	@Override
	public UnequipResponse unequipItem(Long userId, Long inventoryItemId) {
		// 인벤토리 아이템 조회
		InventoryItem inventoryItem = inventoryItemRepository.findByIdAndValidateOwner(inventoryItemId, userId);

		// 아이템 장착 해제
		inventoryItem.unequip();

		// 응답은 장착 해제한 아이템
		return UnequipResponse.fromEntity(inventoryItem);
	}

}
