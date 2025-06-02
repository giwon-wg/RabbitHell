package com.example.rabbithell.domain.monster.repository;

import static com.example.rabbithell.domain.monster.exception.code.MonsterExceptionCode.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.monster.entity.Monster;
import com.example.rabbithell.domain.monster.exception.MonsterException;

public interface MonsterRepository extends JpaRepository<Monster, Long> {
    default Monster findByIdOrElseThrow(long monsterId) {
        return findById(monsterId)
            .orElseThrow(() -> new MonsterException(MONSTER_NOT_FOUND));
    }
}
