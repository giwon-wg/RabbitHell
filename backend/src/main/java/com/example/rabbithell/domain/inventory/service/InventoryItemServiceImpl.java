package com.example.rabbithell.domain.inventory.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.character.entity.Character;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.inventory.dto.request.EquipRequest;
import com.example.rabbithell.domain.inventory.dto.response.EquipResponse;
import com.example.rabbithell.domain.inventory.dto.response.InventoryItemResponse;
import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.entity.InventoryItemId;
import com.example.rabbithell.domain.inventory.repository.InventoryItemRepository;
import com.example.rabbithell.domain.inventory.repository.InventoryRepository;
import com.example.rabbithell.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryItemServiceImpl implements InventoryItemService {

	private final InventoryItemRepository inventoryItemRepository;
	private final InventoryRepository inventoryRepository;
	private final UserRepository userRepository;
	private final CharacterRepository characterRepository;

	@Transactional(readOnly = true)
	@Override
	public InventoryItemResponse getInventoryItemById(Long userId, Long itemId) {
		Inventory inventory = inventoryRepository.findByUserIdOrElseThrow(userId);

		InventoryItemId id = new InventoryItemId(inventory.getId(), itemId);
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

	@Transactional
	@Override
	public EquipResponse equipItem(Long userId, Long itemId, EquipRequest equipRequest) {
		// 현재 로그인한 유저의 인벤토리 조회
		Inventory inventory = inventoryRepository.findByUserIdOrElseThrow(userId);

		// 인벤토리 ID와 아이템 ID로 복합 키를 생성해서 인벤토리 아이템 조회
		InventoryItemId id = new InventoryItemId(inventory.getId(), itemId);
		InventoryItem inventoryItem = inventoryItemRepository.findByIdOrElseThrow(id);

		// 아이템을 장착하기 위해 캐릭터 조회
		Long characterId = equipRequest.characterId();
		Character character = characterRepository.findByIdOrElseThrow(characterId);

		// 아이템 장착
		inventoryItem.equip(character, equipRequest.slot());

		// 응답은 캐릭터가 장착 중인 모든 아이템
		return inventoryItemRepository.findEquipmentStatusByCharacterId(characterId);
	}

}
