package com.example.rabbithell.domain.battle.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.battle.dto.response.BattleResultResponse;
import com.example.rabbithell.domain.battle.dto.response.GetBattleFieldsResponse;
import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.inventory.repository.InventoryItemRepository;
import com.example.rabbithell.domain.inventory.repository.InventoryRepository;
import com.example.rabbithell.domain.monster.service.MonsterService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BattleService {

	private final CharacterRepository characterRepository;
	private final MonsterService monsterService;
	private final InventoryRepository inventoryRepository;
	private final InventoryItemRepository inventoryItemRepository;
	private final CloverRepository cloverRepository;

	public GetBattleFieldsResponse getBattleFields(AuthUser authUser, Long characterId) {

		Clover clover = cloverRepository.findByUserIdOrElseThrow(authUser.getUserId());

		Set<BattleFieldType> maps = clover.getUnlockedRareMaps();

		maps.add(BattleFieldType.PLAIN);
		maps.add(BattleFieldType.MOUNTAIN);
		maps.add(BattleFieldType.FOREST);
		maps.add(BattleFieldType.DESERT);

		return new GetBattleFieldsResponse(maps);
	}

	public BattleResultResponse doBattle(AuthUser authUser, Long characterId, BattleFieldType battleFieldType) {

		// Monster monster = monsterService.getRandomMonster(battleFieldType);
		//
		// Inventory inventory = inventoryRepository.findByUserIdOrElseThrow(character.getUser().getId());
		// List<InventoryItem> items = inventoryItemRepository.findAllByInventoryId(inventory.getId());
		//
		// Item weapon = items.stream()
		// 	.filter(i -> i.getSlot() == Slot.HAND)
		// 	.map(InventoryItem::getItem)
		// 	.findFirst()
		// 	.orElse(null);
		//
		// Item armor = items.stream()
		// 	.filter(i -> i.getSlot() == Slot.BODY)
		// 	.map(InventoryItem::getItem)
		// 	.findFirst()
		// 	.orElse(null);
		//
		// Item accessory = items.stream()
		// 	.filter(i -> i.getSlot() == Slot.HEAD)
		// 	.map(InventoryItem::getItem)
		// 	.findFirst()
		// 	.orElse(null);
		//
		// int playerAttack = (int)(character.getStrength() + (weapon != null ? weapon.getAttack() : 0));
		//
		// int playerDefence = (int)(character.getStrength() + (armor != null ? armor.getAttack() : 0) +
		// 	(accessory != null ? accessory.getAttack() : 0));
		//
		// int playerSpeed = (int)(character.getAgility() - (weapon != null ? weapon.getWeight() : 0) -
		// 	(armor != null ? armor.getWeight() : 0) - (accessory != null ? accessory.getWeight() : 0));
		//
		// //아이템 특수 효과 적용 로직 추가 예정 소켓
		//
		// String log = "";
		//
		// for (int i = 0; i < 30; i++) {
		//
		// }

		return null;
	}
}

