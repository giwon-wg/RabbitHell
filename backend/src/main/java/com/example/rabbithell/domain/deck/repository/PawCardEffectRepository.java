package com.example.rabbithell.domain.deck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.deck.entity.PawCardEffect;

public interface PawCardEffectRepository extends JpaRepository<PawCardEffect, Long>, PawCardEffectQueryRepository {
}
