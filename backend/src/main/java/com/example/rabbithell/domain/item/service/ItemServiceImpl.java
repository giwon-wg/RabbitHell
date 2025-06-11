package com.example.rabbithell.domain.item.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.inventory.repository.InventoryItemRepository;
import com.example.rabbithell.domain.item.dto.request.ItemRequest;
import com.example.rabbithell.domain.item.dto.response.ItemCountResponse;
import com.example.rabbithell.domain.item.dto.response.ItemResponse;
import com.example.rabbithell.domain.item.entity.Item;
import com.example.rabbithell.domain.item.repository.ItemRepository;
import com.example.rabbithell.domain.shop.entity.Shop;
import com.example.rabbithell.domain.shop.repository.ShopRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

	private final ItemRepository itemRepository;
	private final ShopRepository shopRepository;
	private final InventoryItemRepository inventoryItemRepository;

	@Override
	public ItemResponse createItem(ItemRequest itemRequest) {
		Shop shop = itemRequest.shopId() != null ? shopRepository.findByIdOrElseThrow(itemRequest.shopId()) : null;

		Item item = Item.builder()
			.shop(shop)
			.name(itemRequest.name())
			.description(itemRequest.description())
			.itemType(itemRequest.itemType())
			.rarity(itemRequest.rarity())
			.price(itemRequest.price())
			.maxPower(itemRequest.maxPower())
			.minPower(itemRequest.minPower())
			.maxWeight(itemRequest.maxWeight())
			.minWeight(itemRequest.minWeight())
			.maxDurability(itemRequest.maxDurability())
			.build();

		Item savedItem = itemRepository.save(item);
		return ItemResponse.fromEntity(savedItem);
	}

	@Transactional(readOnly = true)
	@Override
	public ItemResponse getItemById(Long itemId) {
		Item item = itemRepository.findByIdOrElseThrow(itemId);
		return ItemResponse.fromEntity(item);
	}

	@Transactional(readOnly = true)
	@Override
	public PageResponse<ItemResponse> getAllItems(Pageable pageable) {
		Page<Item> page = itemRepository.findAll(pageable);

		List<ItemResponse> dtoList = page.getContent().stream()
			.map(ItemResponse::fromEntity)
			.toList();

		return PageResponse.of(dtoList, page);
	}

	@Transactional
	@Override
	public ItemResponse updateItem(Long itemId, ItemRequest itemRequest) {
		Item item = itemRepository.findByIdOrElseThrow(itemId);
		Shop shop = shopRepository.findByIdOrElseThrow(itemRequest.shopId());

		item.update(
			shop,
			itemRequest.name(),
			itemRequest.description(),
			itemRequest.itemType(),
			itemRequest.rarity(),
			itemRequest.price(),
			itemRequest.maxPower(),
			itemRequest.minPower(),
			itemRequest.maxWeight(),
			itemRequest.minWeight(),
			itemRequest.maxDurability()
		);

		return ItemResponse.fromEntity(item);
	}

	@Transactional
	@Override
	public void deleteItem(Long itemId) {
		Item item = itemRepository.findByIdOrElseThrow(itemId);
		item.markAsDeleted();
	}

	@Transactional(readOnly = true)
	@Override
	public List<ItemCountResponse> countItems() {
		return inventoryItemRepository.countInventoryItemsByItem_Id();
	}

}
