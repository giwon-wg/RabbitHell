package com.example.rabbithell.domain.inventory.entity;

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

	private Integer maxDurability;

	private Integer durability;

	private Long weight; // Item 엔티티의 maxWeight와 minWeight 사이

	@Enumerated(EnumType.STRING)
	private Slot slot; // 장착 부위

	@Builder
	public InventoryItem(Inventory inventory, Item item, GameCharacter character, Long power, Integer maxDurability,
		Integer durability, Long weight, Slot slot) {
		this.inventory = inventory;
		this.item = item;
		this.character = character;
		this.power = power;
		this.maxDurability = maxDurability;
		this.durability = durability;
		this.weight = weight;
		this.slot = slot;
	}

	public InventoryItem(Inventory inventory, Item item) {
		this.inventory = inventory;
		this.item = item;
		this.power = (item.getMaxPower() + item.getMinPower()) / 2; // 일단 최대값과 최소값의 평균으로 설정
		this.maxDurability = item.getMaxDurability();
		this.durability = item.getMaxDurability();
		this.weight = (item.getMaxWeight() + item.getMinWeight()) / 2; // 일단 최대값과 최소값의 평균으로 설정
	}

	public void equip(GameCharacter character) {
		this.character = character;
		this.slot = Slot.getSlotByItemType(this.getItem().getItemType());
	}

	public void unequip() {
		this.character = null;
		this.slot = null;
	}

	// TODO: 나중에 기능 수정 필요: 내구도 닳는 양 필요
	public void use(int amount) {
		this.durability -= amount;
	}

}
