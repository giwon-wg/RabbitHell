package com.example.rabbithell.domain.character.entity;

import static com.example.rabbithell.domain.skill.exception.code.SkillExceptionCode.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.example.rabbithell.common.audit.BaseEntity;
import com.example.rabbithell.domain.characterSkill.entity.CharacterSkill;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.job.entity.Job;
import com.example.rabbithell.domain.job.entity.JobCategory;
import com.example.rabbithell.domain.skill.exception.SkillException;
import com.example.rabbithell.domain.user.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.OneToMany;
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
	private int agility;
	private int intelligence;
	private int focus;
	private int luck;

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

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "character_job_tier_history", joinColumns = @JoinColumn(name = "character_id"))
	@MapKeyEnumerated(EnumType.STRING)
	@Column(name = "tier")
	private Map<JobCategory, Integer> jobHistory = new EnumMap<>(JobCategory.class);

	@OneToMany(mappedBy = "character", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CharacterSkill> characterSkills = new ArrayList<>();

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
		int agility,
		int intelligence,
		int focus,
		int luck,
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
		this.skillPoint = skillPoint;
	}

	public int totalSkillPoint() {
		return (warriorPoint + thiefPoint + wizardPoint + archerPoint);
	}

	// 스킬 포인트 증가
	public void addWarriorPoint(int amount) {
		warriorPoint += amount;
		skillPoint += amount;
	}

	public void addThiefPoint(int amount) {
		thiefPoint += amount;
		skillPoint += amount;
	}

	public void addWizardPoint(int amount) {
		wizardPoint += amount;
		skillPoint += amount;
	}

	public void addArcherPoint(int amount) {
		archerPoint += amount;
		skillPoint += amount;
	}

	// 스킬 배울시 스킬포인트 소모
	public void learnSkillBySkillPoint(int amount) {
		if (skillPoint < amount) {
			throw new SkillException(NOT_ENOUGH_SKILL_POINTS);
		}
		this.skillPoint -= amount;
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

	// 전직시 저장
	public void addJobTierHistory(JobCategory category, int tier) {
		Integer currentTier = jobHistory.getOrDefault(category, 0);
		if (tier > currentTier) {
			jobHistory.put(category, tier);
		}
	}

	// 직업군 별로 전직했는지 확인
	public boolean hasExperienced(JobCategory category, int atLeastTier) {
		return jobHistory.getOrDefault(category, 0) >= atLeastTier;
	}

	// 행운
	public void updateLuck(int value) {
		this.luck = value;
	}

}

