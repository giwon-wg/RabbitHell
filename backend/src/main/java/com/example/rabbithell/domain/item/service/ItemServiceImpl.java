package com.example.rabbithell.domain.item.service;

import org.springframework.stereotype.Service;

import com.example.rabbithell.domain.item.dto.request.ItemRequest;
import com.example.rabbithell.domain.item.dto.response.ItemResponse;
import com.example.rabbithell.domain.item.entity.Item;
import com.example.rabbithell.domain.item.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public ItemResponse createItem(ItemRequest itemRequest) {
        Item item = Item.builder()
            .shop(itemRequest.shop())
            .name(itemRequest.name())
            .rarity(itemRequest.rarity())
            .price(itemRequest.price())
            .attack(itemRequest.attack())
            .weight(itemRequest.weight())
            .durability(itemRequest.durability())
            .build();

        Item savedItem = itemRepository.save(item);
        return ItemResponse.fromEntity(savedItem);
    }

}
