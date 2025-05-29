package com.example.rabbithell.domain.village.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.village.entity.Village;

public interface VillageRepository extends JpaRepository<Village, Long> {
}
