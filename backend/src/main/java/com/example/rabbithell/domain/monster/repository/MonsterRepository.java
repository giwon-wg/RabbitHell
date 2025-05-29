package com.example.rabbithell.domain.monster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.monster.entity.Monster;

public interface MonsterRepository extends JpaRepository<Monster, Long> {
}
