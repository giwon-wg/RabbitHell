package com.example.rabbithell.domain.village.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.village.entity.VillageConnection;

public interface VillageConnectionRepository extends JpaRepository<VillageConnection, Long> {

	List<VillageConnection> findByFromVillage(Long fromVillageId);
}
