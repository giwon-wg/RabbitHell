package com.example.rabbithell.domain.monster.repository;

import com.example.rabbithell.domain.monster.entity.Monster;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.monster.entity.DropRate;

import java.util.List;

public interface DropRateRepository extends JpaRepository<DropRate, Long> {
    List<DropRate> findByMonster(Monster monster);
}
