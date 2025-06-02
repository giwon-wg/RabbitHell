package com.example.rabbithell.domain.item.entity;

import com.example.rabbithell.common.audit.BaseEntity;
import com.example.rabbithell.domain.item.enums.ItemType;
import com.example.rabbithell.domain.item.enums.Rarity;
import com.example.rabbithell.domain.shop.entity.Shop;

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
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "item")
public class Item extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shop_id")
	private Shop shop;

	@Column(nullable = false, length = 20)
	private String name;

	@Enumerated(EnumType.STRING)
	private ItemType itemType;

	@Enumerated(EnumType.STRING)
	private Rarity rarity;

	private Long price;

	private Long power; // 아이템 위력

	private Long maxPower;

	private Long minPower;

	private Long weight;

	@Column(nullable = false)
	private Integer durability;

	@Column(nullable = false)
	private boolean isDeleted;

	@Builder
	public Item(Shop shop, String name, ItemType itemType, Rarity rarity, Long price, Long power, Long maxPower,
		Long minPower, Long weight, Integer durability, boolean isDeleted) {
		this.shop = shop;
		this.name = name;
		this.itemType = itemType;
		this.rarity = rarity;
		this.price = price;
		this.power = power;
		this.maxPower = maxPower;
		this.minPower = minPower;
		this.weight = weight;
		this.durability = durability;
		this.isDeleted = isDeleted;
	}

	public void update(Shop shop, String name, ItemType itemType, Rarity rarity, Long price, Long power, Long maxPower,
		Long minPower, Long weight, Integer durability) {
		this.shop = shop;
		this.name = name;
		this.itemType = itemType;
		this.rarity = rarity;
		this.price = price;
		this.power = power;
		this.maxPower = maxPower;
		this.minPower = minPower;
		this.weight = weight;
		this.durability = durability;
	}

	public void markAsDeleted() {
		this.isDeleted = true;
	}

}
