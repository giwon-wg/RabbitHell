package com.example.rabbithell.domain.inventory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.inventory.dto.response.EquipResponse;
import com.example.rabbithell.domain.inventory.dto.response.InventoryResponse;
import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.repository.InventoryItemRepository;
import com.example.rabbithell.domain.inventory.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

	private final InventoryRepository inventoryRepository;
	private final InventoryItemRepository inventoryItemRepository;
	private final CharacterRepository characterRepository;
	private final CloverRepository cloverRepository;

	@Transactional
	@Override
	public InventoryResponse expandInventory(Long userId, int amount) {
		Clover clover = cloverRepository.findByUserIdOrElseThrow(userId);

		Inventory inventory = inventoryRepository.findByCloverOrElseThrow(clover);

		// TODO: 골드 소모

		// 인벤토리 확장
		inventory.expand(amount);

		return InventoryResponse.fromEntity(inventory);
	}

	@Override
	public EquipResponse getEquippedItemsByCharacter(Long userId, Long characterId) {
		// 현재 로그인한 유저의 캐릭터가 맞는지 검증
		//characterRepository.validateOwner(characterId, userId);

		// 캐릭터가 장착한 아이템 반환
		return inventoryItemRepository.findEquipmentStatusByCharacterId(characterId);
	}

}
