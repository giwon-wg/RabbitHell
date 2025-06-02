package com.example.rabbithell.domain.monster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.battle.entity.BattleField;
import com.example.rabbithell.domain.monster.entity.MonsterEncounter;

public interface MonsterEncounterRepository extends JpaRepository<MonsterEncounter, Long> {
    List<MonsterEncounter> findByBattleField(BattleField battleField);
}
