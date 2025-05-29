package com.example.rabbithell.domain.character.repository;

import static com.example.rabbithell.domain.character.exception.code.CharacterExceptionCode.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.character.entity.Character;
import com.example.rabbithell.domain.character.exception.CharacterException;

public interface CharacterRepository extends JpaRepository<Character, Long> {

    default Character findByIdOrElseThrow(Long id) {
        return findById(id)
            .orElseThrow(()-> new CharacterException(CHARACTER_NOT_FOUND));
    }

    List<Character> findByUser_Id(Long userId);

}
