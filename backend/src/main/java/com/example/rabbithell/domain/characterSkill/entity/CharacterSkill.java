package com.example.rabbithell.domain.characterSkill.entity;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.skill.entity.Skill;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class CharacterSkill {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "character_id")
	private GameCharacter character;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "skill_id")
	private Skill skill;

	private boolean equipped;

	private int skillTier;

	public CharacterSkill(GameCharacter character, Skill skill) {
		this.character = character;
		this.skill = skill;
		this.equipped = false;
		this.skillTier = skill.getTier();
	}

	public void equip() {
		this.equipped = true;
	}

	public void unequip() {
		this.equipped = false;
	}

}
