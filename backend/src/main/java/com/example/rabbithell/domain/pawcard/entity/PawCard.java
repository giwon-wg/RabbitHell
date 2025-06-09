package com.example.rabbithell.domain.pawcard.entity;

import org.hibernate.annotations.DynamicUpdate;

import com.example.rabbithell.common.audit.BaseEntity;
import com.example.rabbithell.common.effect.enums.DomainType;
import com.example.rabbithell.common.effect.enums.StatCategory;
import com.example.rabbithell.common.effect.enums.StatType;
import com.example.rabbithell.domain.pawcard.enums.CardEmblem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DynamicUpdate
@Entity
@NoArgsConstructor
@Getter
@Table(name = "paw_card")
public class PawCard extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Integer ratioPercent;

	@Column(nullable = false)
	private String description;

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted;

	@Column(name = "card_number", nullable = false)
	private Integer cardNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "card_emblem", nullable = false)
	private CardEmblem cardEmblem;

	@Enumerated(EnumType.STRING)
	@Column(name = "Stat_type", nullable = false)
	private StatType statType;

	@Enumerated(EnumType.STRING)
	@Column(name = "stat_category", nullable = false)
	private StatCategory statCategory;

	@Enumerated(EnumType.STRING)
	@Column(name = "domain_type", nullable = false)
	private DomainType domainType;

	@Builder
	public PawCard(
		Integer ratioPercent,
		String description,
		Integer cardNumber,
		CardEmblem cardEmblem,
		StatType statType,
		StatCategory statCategory,
		DomainType domainType,
		Boolean isDelete

	) {
		this.ratioPercent = ratioPercent;
		this.description = description;
		this.cardNumber = cardNumber;
		this.cardEmblem = cardEmblem;
		this.statType = statType;
		this.statCategory = statCategory;
		this.domainType = domainType;
		this.isDeleted = isDelete;
	}

	public void markAsDeleted() {
		this.isDeleted = true;
	}

	public void changeRatio(Integer ratioPercent) {
		this.ratioPercent = ratioPercent;
	}

	public void changeDescription(String description) {
		this.description = description;
	}

	public void changeCardNumber(Integer cardNumber) {
		this.cardNumber = cardNumber;
	}

	public void changeCardEmblem(CardEmblem cardEmblem) {
		this.cardEmblem = cardEmblem;
	}

	public void changeStatType(StatType statType) {
		this.statType = statType;
	}

	public void changeStatCategory(StatCategory statCategory) {
		this.statCategory = statCategory;
	}

	public void changeDomainType(DomainType domainType) {
		this.domainType = domainType;
	}
}
