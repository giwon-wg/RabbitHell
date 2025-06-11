package com.example.rabbithell.domain.kingdom.service;

import com.example.rabbithell.domain.kingdom.dto.response.KingdomResponse;

public interface KingdomService {

	KingdomResponse findKingdomById(Long id);

}
