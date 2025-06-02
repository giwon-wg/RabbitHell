package com.example.rabbithell.domain.monster.entity;

import com.example.rabbithell.domain.battle.type.BattleFieldType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class MonsterEncounter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Double spawnRate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "monster_id", nullable = false)
	private Monster monster;

	@Column(nullable = false)
	private BattleFieldType battleFieldType;

	public MonsterEncounter(Double spawnRate, Monster monster, BattleFieldType battleFieldType) {
		this.spawnRate = spawnRate;
		this.monster = monster;
		this.battleFieldType = battleFieldType;
	}

}
