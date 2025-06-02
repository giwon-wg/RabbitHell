package com.example.rabbithell.domain.item.entity;

import com.example.rabbithell.domain.item.enums.EffectType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "effect")
public class Effect {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private EffectType effectType;

	@Column(nullable = false)
	private Long power;

	@Column(nullable = false)
	private boolean isDeleted;

	@Builder
	public Effect(EffectType effectType, Long power, boolean isDeleted) {
		this.effectType = effectType;
		this.power = power;
		this.isDeleted = isDeleted;
	}

	public void update(EffectType effectType, Long power) {
		this.effectType = effectType;
		this.power = power;
	}

	public void markAsDeleted() {
		this.isDeleted = true;
	}

}
