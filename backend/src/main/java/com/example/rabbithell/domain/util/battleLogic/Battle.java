package com.example.rabbithell.domain.util.battleLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.example.rabbithell.domain.battle.enums.BattleResult;
import com.example.rabbithell.domain.battle.vo.BattleResultVo;
import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.monster.entity.Monster;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Battle {

	public BattleResultVo executeBattle(List<GameCharacter> clover, Monster monster) {

		List<Integer> playerAttack = new ArrayList<>();
		List<Integer> playerDefense = new ArrayList<>();
		List<Integer> playerSpeed = new ArrayList<>();
		List<Integer> criticalChances = new ArrayList<>();
		int maxHp = 0, maxMp = 0, currentHp = 0, currentMp = 0;

		// 아이템 추가부분 포카드

		for (GameCharacter rabbit : clover) {
			playerAttack.add(rabbit.getStrength());
			playerDefense.add(rabbit.getStrength());  // 실제로는 Defense 필드 분리 필요
			playerSpeed.add(rabbit.getAgility());
			criticalChances.add(20 + rabbit.getFocus() / 10); // 임의로

			maxHp += rabbit.getMaxHp();
			maxMp += rabbit.getMaxMp();
			currentHp += rabbit.getHp();
			currentMp += rabbit.getMp();
		}

		int integratedDefence = playerDefense.stream().mapToInt(Integer::intValue).sum() / clover.size();

		List<ActionEntity> turnQueue = buildTurnQueue(clover, monster, playerAttack, playerDefense, playerSpeed,
			criticalChances);

		StringBuilder log = new StringBuilder("Battle Result: ");
		int monsterHp = monster.getHp();

		Random random = new Random();

		for (int turn = 0; turn < 30; turn++) {
			if (currentHp <= 0 || monsterHp <= 0)
				break;
			log.append("\n\n[Turn ").append(turn + 1).append("]");

			for (ActionEntity actor : turnQueue) {
				if (currentHp <= 0 || monsterHp <= 0)
					break;

				log.append("\n 현재 HP: ").append(currentHp)
					.append(" 현재 MP: ").append(currentMp)
					.append("\n현재 몬스터 HP: ").append(monsterHp);

				int attackCount = Math.max(1, actor.speed / 50 + random.nextInt(3) - 1);

				if (actor.isPlayer) {
					log.append("\n").append(actor.name).append("의 ").append(attackCount).append("회 공격!");
					for (int i = 0; i < attackCount; i++) {
						int damage = calculateDamage(actor.attack, monster.getDefence(), actor.criticalChance, random,
							log, actor.name);
						monsterHp -= damage;
					}
				} else {
					log.append("\n").append(monster.getMonsterName()).append("의 ").append(attackCount).append("회 공격!");
					for (int i = 0; i < attackCount; i++) {
						int damage = calculateDamage(actor.attack, integratedDefence, actor.criticalChance, random, log,
							monster.getMonsterName());
						currentHp -= damage;
					}
				}
			}
		}

		BattleResult battleResult = (currentHp > 0 && monsterHp <= 0) ? BattleResult.WIN
			: (currentHp <= 0 && monsterHp > 0) ? BattleResult.LOSE
			: BattleResult.DRAW;

		return BattleResultVo.builder()
			.battleResult(battleResult)
			.usedPotionHp(maxHp - currentHp)
			.usedPotionMp(maxMp - currentMp)
			.playerAttack(playerAttack)
			.playerDefense(playerDefense)
			.playerSpeed(playerSpeed)
			.monsterAttack(monster.getAttack())
			.monsterDefence(monster.getDefence())
			.monsterSpeed(monster.getSpeed())
			.log(log.toString())
			.build();
	}

	private List<ActionEntity> buildTurnQueue(List<GameCharacter> clover, Monster monster,
		List<Integer> attacks, List<Integer> defenses, List<Integer> speeds, List<Integer> crits) {

		List<ActionEntity> queue = new ArrayList<>();

		for (int i = 0; i < clover.size(); i++) {
			GameCharacter rabbit = clover.get(i);
			queue.add(new ActionEntity(rabbit.getName(), speeds.get(i), true,
				attacks.get(i), defenses.get(i), crits.get(i), rabbit));
		}

		queue.add(new ActionEntity(monster.getMonsterName(), monster.getSpeed(), false,
			monster.getAttack(), monster.getDefence(), 0, null));

		queue.sort((a, b) -> Integer.compare(b.speed, a.speed));
		return queue;
	}

	private int calculateDamage(int attackerAtk, int defenderDef, int criticalChance, Random random, StringBuilder log,
		String name) {
		int baseDamage = Math.max(1, attackerAtk - defenderDef);
		int minDamage = Math.max(1, baseDamage / 2);
		int maxDamage = Math.max(minDamage, baseDamage);
		int damage = ThreadLocalRandom.current().nextInt(minDamage, maxDamage + 1);

		// 스킬데미지

		if (random.nextInt(100) < criticalChance) {
			damage *= 2;
			log.append("\n 크리티컬!!!");
		}
		log.append("\n").append(name).append("이(가) ").append(damage).append(" 데미지를 입혔습니다.");
		return damage;
	}

	@AllArgsConstructor
	private class ActionEntity {
		String name;
		int speed;
		boolean isPlayer;
		int attack;
		int defence;
		int criticalChance;
		GameCharacter character; // optional
	}
}
