package com.example.rabbithell.domain.monster.service;

import static com.example.rabbithell.domain.monster.exception.code.MonsterExceptionCode.*;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.rabbithell.domain.battle.entity.BattleField;
import com.example.rabbithell.domain.battle.repository.BattleFieldRepository;
import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.monster.entity.Monster;
import com.example.rabbithell.domain.monster.entity.MonsterEncounter;
import com.example.rabbithell.domain.monster.exception.MonsterException;
import com.example.rabbithell.domain.monster.repository.MonsterEncounterRepository;
import com.example.rabbithell.domain.monster.repository.MonsterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MonsterService {

	private final MonsterRepository monsterRepository;
	private final BattleFieldRepository battleFieldRepository;
	private final MonsterEncounterRepository monsterEncounterRepository;

	public Monster getRandomMonster(BattleFieldType battleFieldType) {
		BattleField battleField = battleFieldRepository.findByTypeOrElseThrow(battleFieldType);

		List<MonsterEncounter> encounters = monsterEncounterRepository.findByBattleField(battleField);

		if (encounters.isEmpty()) {
			throw new MonsterException(NO_MONSTER_ON_FIELD);
		}

		double totalRate = encounters.stream()
			.mapToDouble(MonsterEncounter::getSpawnRate)
			.sum();

		double random = Math.random() * totalRate;
		double accumulated = 0.0;

		for (MonsterEncounter encounter : encounters) {
			accumulated += encounter.getSpawnRate();
			if (random <= accumulated) {
				return encounter.getMonster();
			}
		}

		return encounters.get(encounters.size() - 1).getMonster();
	}

}
