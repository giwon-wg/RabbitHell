package com.example.rabbithell.domain.battle.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.battle.dto.response.BattleResultResponse;
import com.example.rabbithell.domain.battle.dto.response.EarnedItemDto;
import com.example.rabbithell.domain.battle.dto.response.GetBattleFieldsResponse;
import com.example.rabbithell.domain.battle.dto.response.ItemDto;
import com.example.rabbithell.domain.battle.postProcess.strategy.BattleRewardStrategy;
import com.example.rabbithell.domain.battle.postProcess.strategy.BattleRewardStrategyFactory;
import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.battle.vo.BattleResultVo;
import com.example.rabbithell.domain.battle.vo.BattleRewardResultVo;
import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.job.entity.Job;
import com.example.rabbithell.domain.monster.entity.Monster;
import com.example.rabbithell.domain.monster.service.MonsterService;
import com.example.rabbithell.domain.util.battleLogic.Battle;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BattleService {

	private final MonsterService monsterService;
	private final CloverRepository cloverRepository;
	private final Battle battle;
	private final BattleRewardStrategyFactory battleRewardStrategyFactory;

	@Transactional
	public GetBattleFieldsResponse getBattleFields(AuthUser authUser, Long characterId) {

		Clover clover = cloverRepository.findByUserIdOrElseThrow(authUser.getUserId());

		Set<BattleFieldType> maps = clover.getUnlockedRareMaps();

		maps.add(BattleFieldType.PLAIN);
		maps.add(BattleFieldType.MOUNTAIN);
		maps.add(BattleFieldType.CAVE);
		maps.add(BattleFieldType.RIFT);

		return new GetBattleFieldsResponse(maps);
	}

	public BattleResultResponse doBattle(AuthUser authUser, BattleFieldType battleFieldType) {

		Monster monster = monsterService.getRandomMonster(battleFieldType);

		Clover clover = cloverRepository.findByUserIdOrElseThrow(authUser.getUserId());

		List<GameCharacter> team = clover.getMembers();
		List<Long> characterIds = team.stream().map(GameCharacter::getId).toList();
		List<Job> jobs = team.stream().map(GameCharacter::getJob).toList();

		BattleResultVo battleResultVo = battle.executeBattle(authUser, team, monster);

		BattleRewardStrategy strategy = battleRewardStrategyFactory.getStrategy(battleResultVo.getBattleResult());
		BattleRewardResultVo reward = strategy.applyReward(clover, team, monster, battleFieldType);

		int usedHpPotion = measurePotion(battleResultVo.getPlayerHp(), team);
		int usedMpPotion = measurePotion(battleResultVo.getPlayerMp(), team);

		List<ItemDto> weapon = new ArrayList<>();
		List<ItemDto> armor = new ArrayList<>();
		List<ItemDto> accessory = new ArrayList<>();

		for (int i = 0; i < team.size(); i++) {
			weapon.add(ItemDto.from(battleResultVo.getWeapon().get(i)));
			armor.add(ItemDto.from(battleResultVo.getArmor().get(i)));
			accessory.add(ItemDto.from(battleResultVo.getAccessory().get(i)));
		}

		List<EarnedItemDto> earnedItemDtos = reward.items().stream()
			.map(EarnedItemDto::from)
			.toList();

		return BattleResultResponse.builder()
			.cloverId(clover.getId())
			.stamina(clover.getStamina())
			.characterIds(characterIds)
			.level(reward.levels())
			.earnedExp(reward.earnedExp())
			.totalExp(reward.totalExps())
			.levelUpAmounts(reward.levelUpAmounts())
			.lostOrEarnedCash(reward.cashDelta())
			.totalCash(reward.totalCash())
			.jobs(jobs)
			.earnedSkillPoint(reward.earnedSkillPoints())
			.totalSkillPoints(reward.totalSkillPoints())
			.increasedStats(reward.increasedStat())
			.battleFieldTypes(reward.unlockedRareMaps())
			.weapon(weapon)
			.armor(armor)
			.accessory(accessory)
			.playerAttack(battleResultVo.getPlayerAttack())
			.playerDefense(battleResultVo.getPlayerDefense())
			.playerSpeed(battleResultVo.getPlayerSpeed())
			.monsterAttack(battleResultVo.getMonsterAttack())
			.monsterDefense(battleResultVo.getMonsterDefense())
			.monsterSpeed(battleResultVo.getMonsterSpeed())
			.battleResult(battleResultVo.getBattleResult())
			.battleLog(battleResultVo.getLog())
			.earnedItems(earnedItemDtos)
			.usedPotionHp(usedHpPotion)
			.usedPotionMp(usedMpPotion)
			.build();
	}

	private int measurePotion(List<Integer> playerStat, List<GameCharacter> team) {
		int result = 0;
		for (int i = 0; i < playerStat.size(); i++) {
			result += team.get(i).getMaxHp() - playerStat.get(i);
		}
		return result;
	}

}

