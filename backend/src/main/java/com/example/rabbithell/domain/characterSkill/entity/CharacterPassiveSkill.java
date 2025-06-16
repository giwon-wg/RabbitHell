package com.example.rabbithell.domain.characterSkill.entity;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.characterSkill.enums.SkillEquipType;
import com.example.rabbithell.domain.skill.entity.PassiveSkill;

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
public class CharacterPassiveSkill {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "character_id")
	private GameCharacter character;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "passive_skill_id")
	private PassiveSkill passiveSkill;

	private int skillTier;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SkillEquipType equipType = SkillEquipType.NONE;

	public CharacterPassiveSkill(GameCharacter character, PassiveSkill passiveSkill) {
		this.character = character;
		this.passiveSkill = passiveSkill;
		this.skillTier = passiveSkill.getTier();
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
