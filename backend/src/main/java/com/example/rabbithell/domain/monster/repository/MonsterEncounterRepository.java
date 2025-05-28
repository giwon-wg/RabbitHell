package com.example.rabbithell.domain.monster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.monster.entity.MonsterEncounter;

public interface MonsterEncounterRepository extends JpaRepository<MonsterEncounter, Long> {
}
