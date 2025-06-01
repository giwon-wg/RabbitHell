package com.example.rabbithell.domain.character.repository;

import static com.example.rabbithell.domain.character.exception.code.CharacterExceptionCode.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.character.exception.CharacterException;

public interface CharacterRepository extends JpaRepository<GameCharacter, Long> {

    default GameCharacter findByIdOrElseThrow(Long id) {
        return findById(id)
            .orElseThrow(()-> new CharacterException(CHARACTER_NOT_FOUND));
    }

    List<GameCharacter> findByUser_Id(Long userId);

}
