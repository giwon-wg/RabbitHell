package com.example.rabbithell.domain.kingdom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.kingdom.entity.Kingdom;

public interface KingdomRepository extends JpaRepository<Kingdom, Long> {
}
