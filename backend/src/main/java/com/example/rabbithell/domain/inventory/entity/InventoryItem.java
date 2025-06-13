package com.example.rabbithell.domain.inventory.entity;

import java.util.Random;

import com.example.rabbithell.common.audit.BaseEntity;
import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.inventory.enums.Slot;
import com.example.rabbithell.domain.item.entity.Item;

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
@Table(name = "inventory_item")
public class InventoryItem extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inventory_id", nullable = false)
	private Inventory inventory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_character_id")
	private GameCharacter character; // 장착 캐릭터

	private Long power; // Item 엔티티의 maxPower와 minPower 사이

	private Integer maxDurability; // 초기값은 Item 엔티티의 maxDurability -> 수리 실패 시 점점 줄어듦

	private Integer durability;

	private Long weight; // Item 엔티티의 maxWeight와 minWeight 사이

	@Enumerated(EnumType.STRING)
	private Slot slot; // 장착 부위

	private Boolean isHidden;

	@Builder
	public InventoryItem(Inventory inventory, Item item, GameCharacter character, Long power, Integer maxDurability,
		Integer durability, Long weight, Slot slot, Boolean isHidden) {
		this.inventory = inventory;
		this.item = item;
		this.character = character;
		this.power = power;
		this.maxDurability = maxDurability;
		this.durability = durability;
		this.weight = weight;
		this.slot = slot;
		this.isHidden = isHidden;
	}

	public InventoryItem(Inventory inventory, Item item) {
		this.inventory = inventory;
		this.item = item;
		this.power = generateSkewedPower(item.getMinPower(), item.getMaxPower());
		this.maxDurability = item.getMaxDurability();
		this.durability = item.getMaxDurability();
		this.weight = generateSkewedWeight(item.getMinWeight(), item.getMaxWeight());
	}

	public void equip(GameCharacter character) {
		this.character = character;
		this.slot = Slot.getSlotByItemType(item.getItemType());
	}

	public void unequip() {
		this.character = null;
		this.slot = null;
	}

	// TODO: 나중에 기능 수정 필요: 내구도 닳는 양 필요
	public void use(int amount) {
		this.durability -= amount;
	}

	public void appraise(Long power, Long weight) {
		this.power = power;
		this.weight = weight;
		this.isHidden = false;
	}

	private long generateSkewedPower(double minPower, double maxPower) {
		// 높은 값은 적게, 낮은 값은 많이 등장
		double skewedRandom = Math.pow(new Random().nextDouble(), 2);
		double weightRange = maxPower - minPower;
		return (long) (minPower + (weightRange * skewedRandom));
	}

	private long generateSkewedWeight(double minWeight, double maxWeight) {
		// 낮은 값은 적게, 높은 값은 많이 등장
		double skewedRandom = 1 - Math.pow(new Random().nextDouble(), 2);
		double weightRange = maxWeight - minWeight;
		return (long) (minWeight + (weightRange * skewedRandom));
	}

}
