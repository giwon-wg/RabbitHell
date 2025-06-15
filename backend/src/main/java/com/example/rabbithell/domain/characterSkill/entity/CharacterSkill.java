package com.example.rabbithell.domain.characterSkill.entity;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.characterSkill.enums.SkillEquipType;
import com.example.rabbithell.domain.skill.entity.Skill;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

	private int skillTier;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SkillEquipType equipType = SkillEquipType.NONE;

	public CharacterSkill(GameCharacter character, Skill skill) {
		this.character = character;
		this.skill = skill;
		this.skillTier = skill.getTier();
	}

	public boolean isEquipped() {
		return equipType != SkillEquipType.NONE;
	}

	public void equip(SkillEquipType type) {
		this.equipType = type;
	}

	public void unequip() {
		this.equipType = SkillEquipType.NONE;
	}

}
