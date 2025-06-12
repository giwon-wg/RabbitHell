package com.example.rabbithell.domain.item.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.item.dto.request.ItemRequest;
import com.example.rabbithell.domain.item.dto.response.ItemCountResponse;
import com.example.rabbithell.domain.item.dto.response.ItemResponse;

import jakarta.validation.Valid;

public interface ItemService {

	ItemResponse createItem(@Valid ItemRequest itemRequest);

	ItemResponse getItemById(Long itemId);

	PageResponse<ItemResponse> getAllItems(Pageable pageable);

	ItemResponse updateItem(Long itemId, @Valid ItemRequest itemRequest);

	void deleteItem(Long itemId);

	List<ItemCountResponse> countItems();

}
