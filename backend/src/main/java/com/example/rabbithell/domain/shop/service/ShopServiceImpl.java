package com.example.rabbithell.domain.shop.service;

import static com.example.rabbithell.domain.shop.exception.code.ShopExceptionCode.*;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.repository.InventoryItemRepository;
import com.example.rabbithell.domain.inventory.repository.InventoryRepository;
import com.example.rabbithell.domain.item.entity.Item;
import com.example.rabbithell.domain.item.repository.ItemRepository;
import com.example.rabbithell.domain.shop.dto.request.AddItemRequest;
import com.example.rabbithell.domain.shop.dto.request.ShopRequest;
import com.example.rabbithell.domain.shop.dto.response.BuyItemResponse;
import com.example.rabbithell.domain.shop.dto.response.SellItemResponse;
import com.example.rabbithell.domain.shop.dto.response.ShopItemResponse;
import com.example.rabbithell.domain.shop.dto.response.ShopResponse;
import com.example.rabbithell.domain.shop.entity.Shop;
import com.example.rabbithell.domain.shop.exception.ShopException;
import com.example.rabbithell.domain.shop.repository.ShopRepository;
import com.example.rabbithell.domain.village.entity.Village;
import com.example.rabbithell.domain.village.repository.VillageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

	private final ShopRepository shopRepository;
	private final VillageRepository villageRepository;
	private final ItemRepository itemRepository;
	private final InventoryItemRepository inventoryItemRepository;
	private final InventoryRepository inventoryRepository;
	private final CloverRepository cloverRepository;

	@Override
	public ShopResponse createShop(ShopRequest shopRequest) {
		Village village = villageRepository.findByIdOrElseThrow(shopRequest.villageId());

		Shop shop = Shop.builder()
			.village(village)
			.name(shopRequest.name())
			.build();

		Shop savedShop = shopRepository.save(shop);
		return ShopResponse.fromEntity(savedShop);
	}

	@Transactional(readOnly = true)
	@Override
	public ShopResponse getShopById(Long shopId) {
		Shop shop = shopRepository.findByIdOrElseThrow(shopId);
		return ShopResponse.fromEntity(shop);
	}

	@Transactional
	@Override
	public ShopResponse updateShop(Long shopId, ShopRequest shopRequest) {
		Shop shop = shopRepository.findByIdOrElseThrow(shopId);

		Village village = villageRepository.findByIdOrElseThrow(shopRequest.villageId());

		shop.update(village, shopRequest.name());

		return ShopResponse.fromEntity(shop);
	}

	@Transactional
	@Override
	public void deleteShop(Long shopId) {
		Shop shop = shopRepository.findByIdOrElseThrow(shopId);
		shop.markAsDeleted();
	}

	@Transactional
	@Override
	public ShopItemResponse addItem(Long shopId, AddItemRequest addItemRequest) {
		Item item = itemRepository.findByIdOrElseThrow(addItemRequest.itemId());
		Shop shop = shopRepository.findByIdOrElseThrow(shopId);

		item.updateShop(shop, addItemRequest.price());

		return ShopItemResponse.fromEntity(item);
	}

	@Transactional(readOnly = true)
	@Override
	public ShopItemResponse getShopItem(Long itemId) {
		Item item = itemRepository.findByIdOrElseThrow(itemId);

		return ShopItemResponse.fromEntity(item);
	}

	@Transactional(readOnly = true)
	@Override
	public PageResponse<ShopItemResponse> getAllShopItems(Long shopId, Pageable pageable) {
		Page<Item> page = itemRepository.findByShop_Id(shopId, pageable);

		List<ShopItemResponse> dtoList = page.stream()
			.map(ShopItemResponse::fromEntity)
			.toList();

		return PageResponse.of(dtoList, page);
	}

	@SuppressWarnings("checkstyle:RegexpMultiline")
	@Transactional
	@Override
	public BuyItemResponse buyItem(Long userId, Long itemId, int quantity) {
		Item item = itemRepository.findByIdOrElseThrow(itemId);
		Clover clover = cloverRepository.findByUserIdOrElseThrow(userId);
		Inventory inventory = inventoryRepository.findByCloverOrElseThrow(clover);

		// 인벤토리 용량 확인
		int availableSlots = getAvailableSlots(inventory);
		if (quantity > availableSlots) {
			throw new ShopException(INVENTORY_FULL);
		}

		// 현금 확인
		long cash = clover.getCash();
		long totalPrice = item.getPrice() * quantity;
		if (cash < totalPrice) {
			throw new ShopException(NOT_ENOUGH_CASH);
		}

		// 현금 차감
		clover.spendCash((int)totalPrice);

		// 인벤토리 아이템 생성 및 저장
		IntStream.range(0, quantity).forEach(i -> {
			InventoryItem inventoryItem = new InventoryItem(inventory, item);
			inventoryItemRepository.save(inventoryItem);
		});

		return BuyItemResponse.of(inventory, item, quantity, getAvailableSlots(inventory), clover.getCash());
	}

	@Transactional
	@Override
	public SellItemResponse sellItem(Long inventoryItemId) {
		InventoryItem inventoryItem = inventoryItemRepository.findByIdOrElseThrow(inventoryItemId);
		Inventory inventory = inventoryItem.getInventory();
		Clover clover = inventoryItem.getCharacter().getClover();
		Item item = inventoryItem.getItem();

		// 가격 확인해서 현금 증가시킴
		clover.earnCash(item.getPrice().intValue());

		// TODO: 장착 중인 아이템은 판매하지 못하도록 수정?
		// TODO: 내구도가 떨어진 아이템은 어떻게 처리할 것인지?

		// 판매한 아이템 인벤토리에서 제거
		inventoryItemRepository.delete(inventoryItem);

		return SellItemResponse.of(inventory, item, getAvailableSlots(inventory), clover.getCash());
	}

	private int getAvailableSlots(Inventory inventory) {
		int usedSlots = inventoryItemRepository.countByInventory_Id(inventory.getId());
		int capacity = inventory.getCapacity();
		return capacity - usedSlots;
	}

}
