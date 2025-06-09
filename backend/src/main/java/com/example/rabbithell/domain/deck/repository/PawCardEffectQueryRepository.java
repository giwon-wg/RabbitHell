package com.example.rabbithell.domain.deck.repository;

import com.example.rabbithell.domain.deck.entity.PawCardEffect;

public interface PawCardEffectQueryRepository {
	PawCardEffect findByCloverIdWithDetails(Long cloverId);
}
