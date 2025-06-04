package com.example.rabbithell.domain.inventory.service;

import static com.example.rabbithell.domain.inventory.exception.code.InventoryItemExceptionCode.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.inventory.dto.request.EquipRequest;
import com.example.rabbithell.domain.inventory.dto.request.UseRequest;
import com.example.rabbithell.domain.inventory.dto.response.EquipResponse;
import com.example.rabbithell.domain.inventory.dto.response.InventoryItemResponse;
import com.example.rabbithell.domain.inventory.dto.response.UnequipResponse;
import com.example.rabbithell.domain.inventory.dto.response.UseResponse;
import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.enums.Slot;
import com.example.rabbithell.domain.inventory.exception.InventoryItemException;
import com.example.rabbithell.domain.inventory.repository.InventoryItemRepository;
import com.example.rabbithell.domain.inventory.repository.InventoryRepository;
import com.example.rabbithell.domain.item.enums.ItemType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryItemServiceImpl implements InventoryItemService {

	private final InventoryItemRepository inventoryItemRepository;
	private final CharacterRepository characterRepository;
	private final InventoryRepository inventoryRepository;
	private final CloverRepository cloverRepository;

	@Transactional(readOnly = true)
	@Override
	public InventoryItemResponse getInventoryItemById(Long userId, Long inventoryItemId) {
		InventoryItem inventoryItem = inventoryItemRepository.findByIdAndValidateOwner(inventoryItemId, userId);

		return InventoryItemResponse.fromEntity(inventoryItem);
	}

	@Transactional(readOnly = true)
	@Override
	public PageResponse<InventoryItemResponse> getAllInventoryItemsFilterBySlot(Long userId, Slot slot,
		Pageable pageable) {
		Inventory inventory = getMyInventory(userId);

		Page<InventoryItem> page;

		// 슬롯 조건이 있으면 적용, 없으면 인벤토리 내 모든 아이템 조회
		if (slot != null) {
			page = inventoryItemRepository.findByInventoryAndSlot(inventory, slot, pageable);
		} else {
			page = inventoryItemRepository.findAll(pageable);
		}

		List<InventoryItemResponse> dtoList = page.getContent().stream()
			.map(InventoryItemResponse::fromEntity)
			.toList();

		return PageResponse.of(dtoList, page);
	}

	@Transactional(readOnly = true)
	@Override
	public PageResponse<InventoryItemResponse> getAllEquipableInventoryItems(Long userId, Pageable pageable) {
		Inventory inventory = getMyInventory(userId);

		// 조건으로 쓰기 위한 장착 가능한 아이템 타입 리스트
		List<ItemType> equipableTypes = ItemType.getEquipableTypes();

		// 장착 가능한 인벤토리 아이템만 조회
		Page<InventoryItem> page = inventoryItemRepository.findByInventoryAndItem_ItemTypeIn(
			inventory, equipableTypes, pageable);

		// DTO로 매핑
		List<InventoryItemResponse> dtoList = page.stream()
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

	@Transactional
	@Override
	public UseResponse useItem(Long userId, Long inventoryItemId, UseRequest useRequest) {
		// 인벤토리 아이템 조회 및 유저 검증
		InventoryItem inventoryItem = inventoryItemRepository.findByIdAndValidateOwner(inventoryItemId, userId);

		// 아이템 타입 체크
		ItemType itemType = inventoryItem.getItem().getItemType();
		if (!(itemType == ItemType.HP || itemType == ItemType.MP)) {
			throw new InventoryItemException(NOT_CONSUMABLE);
		}

		// 아이템 내구도 체크
		if (inventoryItem.getDurability() < useRequest.amount()) {
			throw new InventoryItemException(NOT_ENOUGH_DURABILITY);
		}

		// 아이템 사용
		inventoryItem.use(useRequest.amount());

		// 응답은 사용한 아이템
		return UseResponse.fromEntity(inventoryItem);
	}

	@Transactional
	@Override
	public void discardItem(Long userId, Long inventoryItemId) {
		// 인벤토리 아이템 조회 및 유저 검증
		InventoryItem inventoryItem = inventoryItemRepository.findByIdAndValidateOwner(inventoryItemId, userId);

		// 인벤토리 아이템 삭제
		inventoryItemRepository.delete(inventoryItem);
	}

	// 나의 인벤토리 조회
	private Inventory getMyInventory(Long userId) {
		Clover clover = cloverRepository.findByUserIdOrElseThrow(userId);
		return inventoryRepository.findByCloverOrElseThrow(clover);
	}

}
