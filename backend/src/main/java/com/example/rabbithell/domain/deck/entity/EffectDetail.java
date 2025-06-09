package com.example.rabbithell.domain.deck.entity;

import com.example.rabbithell.common.effect.enums.StatType;
import com.example.rabbithell.domain.deck.enums.EffectDetailSlot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "effect_detail")
public class EffectDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Enumerated(EnumType.STRING)
	private EffectDetailSlot effectDetailSlot;

	@Enumerated(EnumType.STRING)
	private StatType statType;

	@Column(name = "final_effect_value")
	private Integer finalEffectValue;

	@ManyToOne
	@JoinColumn(name = "paw_card_effect_id", nullable = false)
	private PawCardEffect pawCardEffect;

	@Builder
	EffectDetail(
		EffectDetailSlot effectDetailSlot,
		StatType statType,
		Integer finalEffectValue,
		PawCardEffect pawCardEffect
	) {
		this.effectDetailSlot = effectDetailSlot;
		this.statType = statType;
		this.finalEffectValue = finalEffectValue;
		this.pawCardEffect = pawCardEffect;
	}

	public void assignToEffect(PawCardEffect pawCardEffect) {
		this.pawCardEffect = pawCardEffect;
	}

	public void updateEffectDetail(StatType statType, Integer finalEffectValue) {
		this.statType = statType;
		this.finalEffectValue = finalEffectValue;
	}
}
