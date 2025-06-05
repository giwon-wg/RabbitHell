package com.example.rabbithell.domain.shop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.item.entity.Item;
import com.example.rabbithell.domain.item.repository.ItemRepository;
import com.example.rabbithell.domain.shop.dto.request.ShopRequest;
import com.example.rabbithell.domain.shop.dto.response.ShopItemResponse;
import com.example.rabbithell.domain.shop.dto.response.ShopResponse;
import com.example.rabbithell.domain.shop.entity.Shop;
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

	@Transactional(readOnly = true)
	@Override
	public ShopItemResponse getShopItem(Long itemId) {
		Item item = itemRepository.findByIdOrElseThrow(itemId);

		return ShopItemResponse.fromEntity(item);
	}



}
