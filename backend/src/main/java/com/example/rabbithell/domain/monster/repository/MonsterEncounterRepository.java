package com.example.rabbithell.domain.monster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.monster.entity.MonsterEncounter;

public interface MonsterEncounterRepository extends JpaRepository<MonsterEncounter, Long> {

	@Query("SELECT m FROM MonsterEncounter m JOIN FETCH m.monster WHERE m.battleFieldType = :battleFieldType")
	List<MonsterEncounter> findByBattleFieldType(BattleFieldType battleFieldType);
}
