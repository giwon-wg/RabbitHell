package com.example.rabbithell.domain.kingdom.service;

import java.util.List;

import com.example.rabbithell.domain.kingdom.dto.response.KingdomResponse;

public interface KingdomService {

	KingdomResponse findKingdomById(Long id);

	List<KingdomResponse> findAllKingdom();

}
