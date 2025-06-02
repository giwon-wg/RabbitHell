package com.example.rabbithell.domain.inventory.entity;

import com.example.rabbithell.common.audit.BaseEntity;
import com.example.rabbithell.domain.character.entity.Character;
import com.example.rabbithell.domain.inventory.enums.Slot;
import com.example.rabbithell.domain.item.entity.Item;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
@Table(name = "inventory_item")
public class InventoryItem extends BaseEntity {

	// 복합키 시작
	@EmbeddedId
	private InventoryItemId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("inventoryId") // InventoryItemId.inventoryId 매핑
	@JoinColumn(name = "inventory_id")
	private Inventory inventory;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("itemId") // InventoryItemId.itemId 매핑
	@JoinColumn(name = "item_id")
	private Item item;
	// 복합키 끝

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "character_id")
	private Character character; // 장착 캐릭터

	private Integer durability;

	@Enumerated(EnumType.STRING)
	private Slot slot; // 장착 부위

	// TODO: 아이템 종류에 따라 장착 부위가 정해지도록 기능 수정 필요
	public void equip(Character character, Slot slot) {
		this.character = character;
		this.slot = slot;
	}

	public void unequip() {
		this.slot = null;
	}

}
