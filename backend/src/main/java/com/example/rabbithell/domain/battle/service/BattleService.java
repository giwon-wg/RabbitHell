package com.example.rabbithell.domain.battle.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.battle.dto.response.BattleResultResponse;
import com.example.rabbithell.domain.battle.dto.response.GetBattleFieldsResponse;
import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.battle.vo.BattleResultVo;
import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.inventory.repository.InventoryItemRepository;
import com.example.rabbithell.domain.inventory.repository.InventoryRepository;
import com.example.rabbithell.domain.monster.entity.Monster;
import com.example.rabbithell.domain.monster.repository.MonsterRepository;
import com.example.rabbithell.domain.monster.service.MonsterService;
import com.example.rabbithell.domain.util.battleLogic.Battle;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BattleService {

	private final CharacterRepository characterRepository;
	private final MonsterService monsterService;
	private final InventoryRepository inventoryRepository;
	private final InventoryItemRepository inventoryItemRepository;
	private final CloverRepository cloverRepository;
	private final MonsterRepository monsterRepository;

	public GetBattleFieldsResponse getBattleFields(AuthUser authUser, Long characterId) {

		Clover clover = cloverRepository.findByUserIdOrElseThrow(authUser.getUserId());

		Set<BattleFieldType> maps = clover.getUnlockedRareMaps();

		maps.add(BattleFieldType.PLAIN);
		maps.add(BattleFieldType.MOUNTAIN);
		maps.add(BattleFieldType.FOREST);
		maps.add(BattleFieldType.DESERT);

		return new GetBattleFieldsResponse(maps);
	}

	public BattleResultResponse doBattle(AuthUser authUser, BattleFieldType battleFieldType) {

		// Monster monster = monsterService.getRandomMonster(battleFieldType);

		Monster monster = monsterRepository.findByIdOrElseThrow(1);

		Clover clover = cloverRepository.findByUserIdOrElseThrow(authUser.getUserId());

		List<GameCharacter> team = clover.getMembers();
		List<Long> characterIds = team.stream()
			.map(GameCharacter::getId)
			.toList();

		Battle battle = new Battle();
		BattleResultVo battleResultVo = battle.executeBattle(team, monster);

		// clover.useStamina(1); --> stamina 사용 로직

		System.out.println("Battle result: " + battleResultVo);

		return null;
	}
}

