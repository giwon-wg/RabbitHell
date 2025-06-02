package com.example.rabbithell.domain.character.entity;

import com.example.rabbithell.common.audit.BaseEntity;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.job.entity.Job;
import com.example.rabbithell.domain.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "game_character", indexes = {@Index(name = "idx_character_id_user_id", columnList = "id, user_id")})
public class GameCharacter extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clover_id")
	private Clover clover;

	@Column(nullable = false, unique = true, length = 10)
	private String name;

	private Job job;

	private int level;
	private int exp;

	private int maxHp;
	private int hp;
	private int maxMp;
	private int mp;

	private int strength;
	private int minStrength;
	private int agility;
	private int minAgility;
	private int intelligence;
	private int minIntelligence;
	private int focus;
	private int minFocus;
	private int luck;
	private int minLuck;

	@Column(name = "incompetent_point")
	private int incompetentPoint;

	@Column(name = "warrior_point")
	private int warriorPoint;

	@Column(name = "thief_point")
	private int thiefPoint;

	@Column(name = "wizard_point")
	private int wizardPoint;

	@Column(name = "archer_point")
	private int archerPoint;

	@Column(name = "skill_point")
	private int skillPoint;

	@Builder
	public GameCharacter(User user,
		Clover clover,
		String name,
		Job job,
		int level,
		int exp,
		int maxHp,
		int hp,
		int maxMp,
		int mp,
		int strength,
		int minxStrength,
		int agility,
		int minAgility,
		int intelligence,
		int minIntelligence,
		int focus,
		int minFocus,
		int luck,
		int minLuck,
		int incompetentPoint,
		int warriorPoint,
		int thiefPoint,
		int wizardPoint,
		int archerPoint,
		int skillPoint
	) {
		this.user = user;
		this.clover = clover;
		this.name = name;
		this.job = job;
		this.level = level;
		this.exp = exp;
		this.maxHp = maxHp;
		this.hp = hp;
		this.maxMp = maxMp;
		this.mp = mp;
		this.strength = strength;
		this.agility = agility;
		this.intelligence = intelligence;
		this.focus = focus;
		this.luck = luck;
		this.incompetentPoint = incompetentPoint;
		this.warriorPoint = warriorPoint;
		this.thiefPoint = thiefPoint;
		this.wizardPoint = wizardPoint;
		this.archerPoint = archerPoint;
		this.skillPoint = (warriorPoint + thiefPoint + wizardPoint + archerPoint);
	}

	public void refill() {
		this.hp = this.maxHp;
		this.mp = this.maxMp;
	}

	public void updateJob(Job newJob) {
		this.job = newJob;
	}

	public void updateStrength(int value) {
		this.strength = value;
	}

	public void updateAgility(int value) {
		this.agility = value;
	}

	public void updateIntelligence(int value) {
		this.intelligence = value;
	}

	public void updateFocus(int value) {
		this.focus = value;
	}


}

