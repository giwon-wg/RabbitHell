package com.example.rabbithell.domain.deck.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.deck.enums.CardRanking;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "paw_card_effect")
public class PawCardEffect {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Enumerated(EnumType.STRING)
	private CardRanking cardRanking;

	@ManyToOne
	@JoinColumn(name = "clover_id", nullable = false)
	Clover clover;

	@OneToMany(mappedBy = "pawCardEffect", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EffectDetail> details = new ArrayList<>();

	@Builder
	PawCardEffect(
		CardRanking cardRanking,
		Clover clover
	) {
		this.cardRanking = cardRanking;
		this.clover = clover;
	}

	public void addEffectDetail(EffectDetail effectDetail) {
		this.details.add(effectDetail);
		effectDetail.assignToEffect(this);
	}

	public void markCardRanking(CardRanking cardRanking) {
		this.cardRanking = cardRanking;
	}

	public void assignClover(Clover clover) {
		this.clover = clover;
	}
}
