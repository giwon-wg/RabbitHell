package com.example.rabbithell.domain.monster.entity;

import com.example.rabbithell.domain.monster.enums.Rating;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Monster {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private Rating rating;

	@Column(nullable = false)
	private String monsterName;

	@Column(nullable = false)
	private int hp;

	@Column(nullable = false)
	private int attack;

	@Column(nullable = false)
	private int defence;

	@Column(nullable = false)
	private int speed;

	public Monster(Rating rating, String monsterName, int hp, int attack, int defence, int speed) {
		this.rating = rating;
		this.monsterName = monsterName;
		this.hp = hp;
		this.attack = attack;
		this.defence = defence;
		this.speed = speed;
	}
}
