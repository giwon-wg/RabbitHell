package com.example.rabbithell.domain.village.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.village.entity.VillageConnection;

public interface VillageConnectionRepository extends JpaRepository<VillageConnection, Long> {
}
