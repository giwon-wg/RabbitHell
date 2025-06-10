package com.example.rabbithell.domain.inventory.service;

import static com.example.rabbithell.domain.inventory.exception.code.InventoryItemExceptionCode.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.inventory.dto.request.UseRequest;
import com.example.rabbithell.domain.inventory.dto.response.EquipResponse;
import com.example.rabbithell.domain.inventory.dto.response.EquipableItemResponse;
import com.example.rabbithell.domain.inventory.dto.response.InventoryItemResponse;
import com.example.rabbithell.domain.inventory.dto.response.UnequipResponse;
import com.example.rabbithell.domain.inventory.dto.response.UseResponse;
import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.enums.Slot;
import com.example.rabbithell.domain.inventory.exception.InventoryItemException;
import com.example.rabbithell.domain.inventory.repository.InventoryItemRepository;
import com.example.rabbithell.domain.inventory.repository.InventoryRepository;
import com.example.rabbithell.domain.item.entity.Item;
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
	public PageResponse<EquipableItemResponse> getAllEquipableInventoryItems(Long userId, Slot slot,
		Pageable pageable) {
		Inventory inventory = getMyInventory(userId);

		Page<InventoryItem> page = inventoryItemRepository.findEquipableItemBySlot(inventory, slot, pageable);

		// DTO로 매핑
		List<EquipableItemResponse> dtoList = page.stream()
			.map(EquipableItemResponse::fromEntity)
			.toList();

		return PageResponse.of(dtoList, page);
	}

	@Transactional(readOnly = true)
	@Override
	public EquipResponse getEquippedItemsByCharacter(Long userId, Long characterId) {
		// 현재 로그인한 유저의 캐릭터가 맞는지 검증
		characterRepository.validateOwner(characterId, userId);

		// 캐릭터가 장착한 아이템 반환
		return inventoryItemRepository.findEquipmentStatusByCharacter(characterId);
	}

	@Cacheable(key = "#characterId", value = "equippedItems")
	@Transactional(readOnly = true)
	@Override
	public List<Item> getEquippedItemsByCharacter(Long characterId) {
		List<InventoryItem> inventoryItems = inventoryItemRepository.findByCharacter_Id(characterId);

		return inventoryItems.stream()
			.map(InventoryItem::getItem)
			.toList();
	}

	@Cacheable(key = "#characterId", value = "equippedInventoryItems")
	@Transactional(readOnly = true)
	@Override
	public List<InventoryItem> getEquippedInventoryItemsByCharacter(Long characterId) {
		return inventoryItemRepository.findByCharacter_Id(characterId);
	}

	@Transactional
	@Override
	public EquipResponse equipItem(Long userId, Long inventoryItemId, Long characterId) {
		// 인벤토리 아이템 조회
		InventoryItem inventoryItem = inventoryItemRepository.findByIdAndValidateOwner(inventoryItemId, userId);

		// 아이템을 장착하기 위해 캐릭터 조회
		GameCharacter character = characterRepository.findByIdOrElseThrow(characterId);

		// 캐릭터가 장착 중인 아이템 조회해서 같은 부위에 아이템이 있으면 그 아이템 장착 해제
		Slot slot = Slot.getSlotByItemType(inventoryItem.getItem().getItemType());
		Long equippedItemId = inventoryItemRepository.findByCharacterAndSlot(characterId, slot);
		if (equippedItemId != null) {
			inventoryItemRepository.findByIdOrElseThrow(equippedItemId).unequip();
		}

		// 아이템 장착
		inventoryItem.equip(character);

		// 응답은 캐릭터가 장착 중인 모든 아이템
		return inventoryItemRepository.findEquipmentStatusByCharacter(characterId);
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

	@Transactional
	@Override
	public InventoryItemResponse appraiseItem(Long userId, Long inventoryItemId) {
		// 인벤토리 아이템 조회 및 유저 검증
		InventoryItem inventoryItem = inventoryItemRepository.findByIdAndValidateOwner(inventoryItemId, userId);

		// 아이템이 숨겨진 상태가 아니면 예외 발생
		if (!inventoryItem.getIsHidden()) {
			throw new InventoryItemException(NOT_HIDDEN);
		}

		// 아이템 감정 로직 시작
		Item item = inventoryItem.getItem();
		Long minPower = item.getMinPower();
		Long maxPower = item.getMaxPower();
		Long minWeight = item.getMinWeight();
		Long maxWeight = item.getMaxWeight();

		double random = ThreadLocalRandom.current().nextDouble(); // 0.0 ~ 1.0 사이
		double skewedToMin = Math.pow(random, 2); // 낮은 값이 나올 확률이 높게 조정
		Long power = minPower + (long)((maxPower - minPower + 1) * skewedToMin);

		random = ThreadLocalRandom.current().nextDouble();
		double skewedToMax = Math.pow(random, 0.5); // 높은 값이 나올 확률이 높게 조정
		Long weight = minWeight + (long)((maxWeight - minWeight + 1) * skewedToMax);

		// 아이템 스탯 확정
		inventoryItem.appraise(power, weight);
		return InventoryItemResponse.fromEntity(inventoryItem);
	}

	// 나의 인벤토리 조회
	private Inventory getMyInventory(Long userId) {
		Clover clover = cloverRepository.findByUserIdOrElseThrow(userId);
		return inventoryRepository.findByCloverOrElseThrow(clover);
	}

}
