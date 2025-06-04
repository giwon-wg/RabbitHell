package com.example.rabbithell.domain.inventory.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.example.rabbithell.domain.inventory.dto.response.EquipResponse;
import com.example.rabbithell.domain.inventory.dto.response.EquippedItem;
import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.entity.QInventoryItem;
import com.example.rabbithell.domain.inventory.enums.Slot;
import com.example.rabbithell.domain.item.entity.QItem;
import com.example.rabbithell.domain.item.enums.ItemType;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class InventoryItemQueryRepositoryImpl implements InventoryItemQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public EquipResponse findEquipmentStatusByCharacter(Long characterId) {
		QInventoryItem inventoryItem = QInventoryItem.inventoryItem;

		List<Tuple> tuples = queryFactory
			.select(inventoryItem.id, inventoryItem.item.id, inventoryItem.item.name, inventoryItem.character.id,
				inventoryItem.character.name, inventoryItem.item.description, inventoryItem.item.power,
				inventoryItem.slot, inventoryItem.durability)
			.from(inventoryItem)
			.where(inventoryItem.character.id.eq(characterId))
			.fetch();

		List<EquippedItem> equippedItems = tuples.stream()
			.map(t -> new EquippedItem(
				t.get(inventoryItem.id),
				t.get(inventoryItem.item.id),
				t.get(inventoryItem.item.name),
				t.get(inventoryItem.character.id),
				t.get(inventoryItem.character.name),
				t.get(inventoryItem.item.description),
				t.get(inventoryItem.item.power),
				t.get(inventoryItem.slot),
				t.get(inventoryItem.durability)
			))
			.toList();

		return new EquipResponse(equippedItems);
	}

	// 슬롯을 쿼리 파라미터 조건으로 해서 장착 가능한 아이템 리스트 조회
	@Override
	public Page<InventoryItem> findEquipableItemBySlot(Inventory inventory, Slot slot, Pageable pageable) {
		QInventoryItem inventoryItem = QInventoryItem.inventoryItem;
		QItem item = QItem.item;

		// 조건으로 쓰기 위한 장착 가능한 아이템 타입 리스트
		List<ItemType> equipableTypes = ItemType.getEquipableTypes();

		// 슬롯 조건
		List<ItemType> itemTypesBySlot = ItemType.getItemTypeBySlot(slot);

		List<InventoryItem> content = queryFactory
			.selectFrom(inventoryItem)
			.join(inventoryItem.item, item).fetchJoin()
			.where(
				inventoryItem.inventory.eq(inventory),
				item.itemType.in(equipableTypes),
				inventoryItem.character.isNull(),
				inventoryItem.slot.isNull(),
				itemTypeIn(item, itemTypesBySlot) // 동적 조건
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(inventoryItem.count())
			.from(inventoryItem)
			.join(inventoryItem.item, item)
			.where(
				inventoryItem.inventory.eq(inventory),
				item.itemType.in(equipableTypes),
				inventoryItem.character.isNull(),
				inventoryItem.slot.isNull(),
				itemTypeIn(item, itemTypesBySlot) // 동적 조건
			);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	private BooleanExpression itemTypeIn(QItem item, List<ItemType> itemTypes) {
		return (itemTypes != null && !itemTypes.isEmpty()) ? item.itemType.in(itemTypes) : null;
	}
}
