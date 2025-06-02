package com.example.rabbithell.domain.inventory.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.rabbithell.domain.inventory.dto.response.EquipResponse;
import com.example.rabbithell.domain.inventory.dto.response.EquippedItem;
import com.example.rabbithell.domain.inventory.entity.QInventoryItem;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class InventoryItemQueryRepositoryImpl implements InventoryItemQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public EquipResponse findEquipmentStatusByCharacterId(Long characterId) {
		QInventoryItem inventoryItem = QInventoryItem.inventoryItem;

		List<Tuple> tuples = queryFactory
			.select(inventoryItem.item.id, inventoryItem.character.id, inventoryItem.slot, inventoryItem.durability)
			.from(inventoryItem)
			.where(inventoryItem.character.id.eq(characterId))
			.fetch();

		List<EquippedItem> equippedItems = tuples.stream()
			.map(t -> new EquippedItem(
				t.get(inventoryItem.id),
				t.get(inventoryItem.item.id),
				t.get(inventoryItem.character.id),
				t.get(inventoryItem.slot),
				t.get(inventoryItem.durability)
			))
			.toList();

		return new EquipResponse(equippedItems);
	}

}
