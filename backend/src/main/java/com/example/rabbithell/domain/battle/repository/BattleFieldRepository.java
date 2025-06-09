package com.example.rabbithell.domain.battle.repository;

import static com.example.rabbithell.domain.battle.exception.code.BattleExceptionCode.*;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.battle.entity.BattleField;
import com.example.rabbithell.domain.battle.exception.BattleException;
import com.example.rabbithell.domain.battle.type.BattleFieldType;

public interface BattleFieldRepository extends JpaRepository<BattleField, Long> {

	Optional<BattleField> findByBattleFieldType(BattleFieldType battleFieldType);

	default BattleField findByTypeOrElseThrow(BattleFieldType battleFieldType) {
		return findByBattleFieldType(battleFieldType)
			.orElseThrow(() -> new BattleException(BATTLEFIELD_NOT_FOUND));
	}
}
