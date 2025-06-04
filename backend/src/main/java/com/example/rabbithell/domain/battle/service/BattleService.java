package com.example.rabbithell.domain.battle.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.battle.dto.response.BattleResultResponse;
import com.example.rabbithell.domain.battle.dto.response.GetBattleFieldsResponse;
import com.example.rabbithell.domain.battle.enums.BattleResult;
import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.battle.vo.BattleResultVo;
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

		Monster monster = monsterService.getRandomMonster(battleFieldType);

		Clover clover = cloverRepository.findByUserIdOrElseThrow(authUser.getUserId());

		List<GameCharacter> team = clover.getMembers();
		List<Long> characterIds = team.stream()
			.map(GameCharacter::getId)
			.toList();
		List<Integer> levels = team.stream()
			.map(GameCharacter::getLevel)
			.toList();

		List<Integer> totalExps = team.stream()
			.map(GameCharacter::getExp)
			.toList();

		List<Integer> skillPoints = team.stream()
			.map(GameCharacter::getSkillPoint)
			.toList();
		List<Job> jobs = team.stream()
			.map(GameCharacter::getJob)
			.toList();

		List<Integer> jobSkillPoints = new ArrayList<>();
		for (GameCharacter member : team) {
			jobSkillPoints.add(member.getSkillPoint());
		}

		BattleResultVo battleResultVo = battle.executeBattle(authUser, team, monster);

		// clover.useStamina(1); --> stamina 사용 로직
		int earnedExp;
		int earnedSkillPoints;
		List<Integer> changedJobSkillPoints = new ArrayList<>();
		List<Integer> totalSkillPoints = new ArrayList<>();

		long lostOrEarnedCash = 0;
		long totalCash = clover.getCash();
		List<Integer> levelUpAmounts = new ArrayList<>();
		List<Integer> changedLevels = new ArrayList<>();

		Set<BattleFieldType> battleFieldTypes = new HashSet<>();
		battleFieldTypes.add(BattleFieldType.PLAIN);
		battleFieldTypes.add(BattleFieldType.MOUNTAIN);
		battleFieldTypes.add(BattleFieldType.FOREST);
		battleFieldTypes.add(BattleFieldType.DESERT);

		if (battleResultVo.getBattleResult() == BattleResult.WIN) {

			// 골드 관련
			lostOrEarnedCash = 1000L;

			totalCash += lostOrEarnedCash;

			// 경험치 관련
			earnedExp = monster.getExp();
			earnedSkillPoints = battleFieldType.getSkillPoints();

			List<Integer> updatedTotalExps = totalExps.stream()
				.map(exp -> Math.min(exp + earnedExp, 9900))
				.toList();

			IntStream.range(0, levels.size()).forEach(i -> {
				int currentLevel = levels.get(i);
				int totalExp = updatedTotalExps.get(i);
				int expectedLevel = totalExp / 100 + 1;

				changedLevels.add(expectedLevel);
				levelUpAmounts.add(Math.max(expectedLevel - currentLevel, 0));

				changedJobSkillPoints.add(jobSkillPoints.get(i) + earnedSkillPoints);
				totalSkillPoints.add(skillPoints.get(i) + earnedSkillPoints);
			});

			// command pattern, strategy pattern

			totalExps = updatedTotalExps;

		} else {
			earnedExp = 0;
			earnedSkillPoints = 0;

			if (battleResultVo.getBattleResult() == BattleResult.LOSE) {
				lostOrEarnedCash = -totalCash;
			} else {

			}
		}
		totalCash += lostOrEarnedCash;

		System.out.println("Battle result: " + battleResultVo);

		return BattleResultResponse.builder()
			.cloverId(clover.getId())
			.stamina(clover.getStamina())
			.characterIds(characterIds)
			.level(levels)
			.earnedExp(earnedExp)
			.totalExp(totalExps)
			.level(changedLevels)
			.levelUpAmounts(levelUpAmounts)
			.lostOrEarnedCash(lostOrEarnedCash)
			.totalCash(totalCash)
			.jobs(jobs)
			.earnedSkillPoint(earnedSkillPoints)
			.totalSkillPoints(totalSkillPoints)
			.jobSkillPoints(jobSkillPoints)
			.battleFieldTypes(battleFieldTypes)
			.weapon(battleResultVo.getWeapon())
			.armor(battleResultVo.getArmor())
			.accessory(battleResultVo.getAccessory())
			.playerAttack(battleResultVo.getPlayerAttack())
			.playerDefense(battleResultVo.getPlayerDefense())
			.playerSpeed(battleResultVo.getPlayerSpeed())
			.monsterAttack(battleResultVo.getMonsterAttack())
			.monsterDefense(battleResultVo.getMonsterDefense())
			.monsterSpeed(battleResultVo.getMonsterSpeed())
			.battleResult(battleResultVo.getBattleResult())
			.battleLog(battleResultVo.getLog())
			.earnedItem(null)
			.usedPotionHp(battleResultVo.getUsedPotionHp())
			.usedPotionMp(battleResultVo.getUsedPotionMp())
			.build();
	}

	private int getJobSkillPoint(GameCharacter member) {
		return switch (member.getJob().getJobCategory()) {
			case INCOMPETENT -> member.getIncompetentPoint();
			case WARRIOR -> member.getWarriorPoint();
			case THIEF -> member.getThiefPoint();
			case WIZARD -> member.getWizardPoint();
			case ARCHER -> member.getArcherPoint();
		};
	}

}

