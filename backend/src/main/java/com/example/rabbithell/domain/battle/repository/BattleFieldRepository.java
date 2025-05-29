package com.example.rabbithell.domain.battle.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.battle.entity.BattleField;

public interface BattleFieldRepository extends JpaRepository<BattleField, Long> {

}
