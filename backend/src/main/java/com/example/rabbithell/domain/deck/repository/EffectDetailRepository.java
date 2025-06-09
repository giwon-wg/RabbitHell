package com.example.rabbithell.domain.deck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.deck.entity.EffectDetail;

public interface EffectDetailRepository extends JpaRepository<EffectDetail, Long> {
}
