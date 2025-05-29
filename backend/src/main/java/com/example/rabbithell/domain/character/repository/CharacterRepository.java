package com.example.rabbithell.domain.character.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.character.entity.Character;

public interface CharacterRepository extends JpaRepository<Character, Long> {
}
