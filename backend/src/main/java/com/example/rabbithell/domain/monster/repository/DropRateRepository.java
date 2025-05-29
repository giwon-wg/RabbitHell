package com.example.rabbithell.domain.monster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.monster.entity.DropRate;

public interface DropRateRepository extends JpaRepository<DropRate, Long> {
}
