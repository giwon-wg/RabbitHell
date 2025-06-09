package com.example.rabbithell.domain.monster.entity;

import java.math.BigDecimal;

import com.example.rabbithell.domain.item.entity.Item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "drop_rate")
public class DropRate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "monster_id", nullable = false)
	private Monster monster;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@Column(name = "rate", nullable = false, precision = 5, scale = 4)
	private BigDecimal rate; // 드랍율 (예: 0.1234 = 12.34%)

	@Builder
	public DropRate(Monster monster, Item item, BigDecimal rate) {
		this.monster = monster;
		this.item = item;
		this.rate = rate;
	}
}
