package com.example.rabbithell.domain.item.service;

import org.springframework.data.domain.Pageable;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.item.dto.request.ItemRequest;
import com.example.rabbithell.domain.item.dto.response.ItemResponse;

import jakarta.validation.Valid;

public interface ItemService {

    ItemResponse createItem(@Valid ItemRequest itemRequest);

    PageResponse<ItemResponse> getAllItems(Pageable pageable);

}
