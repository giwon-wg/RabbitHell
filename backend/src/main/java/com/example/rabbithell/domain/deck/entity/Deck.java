package com.example.rabbithell.domain.deck.entity;

import com.example.rabbithell.common.audit.BaseEntity;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.deck.enums.PawCardSlot;
import com.example.rabbithell.domain.pawcard.entity.PawCard;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "deck",
	indexes = {
		@Index(name = "idx_clover_id", columnList = "clover_id")
	},
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"clover_id", "paw_card_slot"}),
		@UniqueConstraint(columnNames = {"clover_id", "paw_card_id"})
	})
public class Deck extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "paw_card_id", nullable = false)
	private PawCard pawCard;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clover_id", nullable = false)
	private Clover clover;

	@Column(name = "paw_card_slot", nullable = true)
	private PawCardSlot pawCardSlot;

	@Builder
	public Deck(
		PawCard pawCard,
		Clover clover
	) {
		this.clover = clover;
		this.pawCard = pawCard;
	}
}
