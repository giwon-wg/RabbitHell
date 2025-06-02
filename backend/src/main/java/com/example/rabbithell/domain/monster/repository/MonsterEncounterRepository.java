package com.example.rabbithell.domain.monster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.monster.entity.MonsterEncounter;

public interface MonsterEncounterRepository extends JpaRepository<MonsterEncounter, Long> {
	List<MonsterEncounter> findByBattleFieldType(BattleFieldType battleFieldType);
}
