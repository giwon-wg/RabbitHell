package com.example.rabbithell.domain.battleField.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.battleField.entity.BattleField;

public interface BattleFieldRepository extends JpaRepository<BattleField, Long> {
}
