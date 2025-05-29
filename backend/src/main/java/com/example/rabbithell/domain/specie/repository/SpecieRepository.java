package com.example.rabbithell.domain.specie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.specie.entity.Specie;

public interface SpecieRepository extends JpaRepository<Specie, Long> {
}
