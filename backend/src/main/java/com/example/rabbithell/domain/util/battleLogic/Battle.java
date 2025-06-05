package com.example.rabbithell.domain.util.battleLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.battle.enums.BattleResult;
import com.example.rabbithell.domain.battle.vo.BattleResultVo;
import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.inventory.service.InventoryItemService;
import com.example.rabbithell.domain.item.entity.Item;
import com.example.rabbithell.domain.item.enums.ItemType;
import com.example.rabbithell.domain.job.entity.Job;
import com.example.rabbithell.domain.monster.entity.Monster;
import com.example.rabbithell.domain.skill.entity.Skill;

import lombok.AllArgsConstructor;

@Component
public class Battle {

	private final InventoryItemService inventoryItemService;

	@Autowired
	public Battle(InventoryItemService inventoryItemService) {
		this.inventoryItemService = inventoryItemService;
	}

	@Transactional
	public BattleResultVo executeBattle(AuthUser authUser, List<GameCharacter> clover, Monster monster) {

		List<Integer> playerHp = new ArrayList<>();
		List<Integer> playerMp = new ArrayList<>();
		List<Integer> playerAttack = new ArrayList<>();
		List<Integer> playerMagic = new ArrayList<>();
		List<Integer> playerDefense = new ArrayList<>();
		List<Integer> playerSpeed = new ArrayList<>();
		List<Integer> criticalChances = new ArrayList<>();
		List<Job> jobs = new ArrayList<>();

		// 아이템 추가부분 포카드
		List<Item> weapons = new ArrayList<>();
		List<Item> armors = new ArrayList<>();
		List<Item> accessories = new ArrayList<>();

		Item weapon = null, armor = null, accessory = null;

		List<List<Skill>> skills = new ArrayList<>();

		for (GameCharacter rabbit : clover) {

			// EquipResponse response = inventoryService.getEquippedItemsByCharacter(authUser.getUserId(), rabbit.getId());
			//
			// List<EquippedItem> equippedItems = response.equippedItems();
			// List<Long> equippedItemIds = new ArrayList<>();
			// for (EquippedItem equippedItem : equippedItems) {
			// 	equippedItemIds.add(equippedItem.itemId());
			//
			// }

			jobs.add(rabbit.getJob());

			// skills.add(rabbit.getSkill());

			List<Item> equipments = inventoryItemService.getEquippedItemsByCharacter(rabbit.getId());
			for (Item equipment : equipments) {
				if (equipment.getItemType() == ItemType.BOW || equipment.getItemType() == ItemType.DAGGER
					|| equipment.getItemType() == ItemType.SWORD) {
					weapon = equipment;
					weapons.add(equipment);
				} else if (equipment.getItemType() == ItemType.SHIELD) {
					armor = equipment;
					armors.add(equipment);
				} else if (equipment.getItemType() == ItemType.SHIELD) {//ACCESSORY
					accessory = equipment;
					accessories.add(equipment);
				}
			}

			playerHp.add(rabbit.getHp());
			playerMp.add(rabbit.getMp());
			if (weapon != null && weapon.getItemType() == ItemType.HP) {//WAND
				playerAttack.add(rabbit.getStrength());
				playerMagic.add((int)(rabbit.getIntelligence() + weapon.getPower()));
			} else {
				playerAttack.add((int)(rabbit.getStrength() + (weapon != null ? weapon.getPower() : 0)));
				playerMagic.add(rabbit.getIntelligence());
			}

			playerDefense.add(
				(int)(100 + (armor != null ? armor.getPower() : 0) + (accessory != null ? accessory.getPower() :
					0)));
			playerSpeed.add((int)(rabbit.getAgility() -
				(weapon != null ? weapon.getWeight() : 0) -
				(armor != null ? armor.getWeight() : 0) -
				(accessory != null ? accessory.getWeight() : 0)
			));
			criticalChances.add(20 + rabbit.getFocus() / 10);

		}

		List<ActionEntity> turnQueue = buildTurnQueue(clover, monster, playerHp, playerMp, playerAttack, playerMagic,
			playerDefense,
			playerSpeed,
			criticalChances);

		StringBuilder log = new StringBuilder("Battle Result: ");
		int monsterHp = monster.getHp();

		Random random = new Random();

		for (int turn = 0; turn < 30; turn++) {
			if (playerHpCheck(playerHp) || monsterHp <= 0)
				break;

			log.append("\n\n[Turn ").append(turn + 1).append("]");

			for (ActionEntity actor : turnQueue) {
				if (playerHpCheck(playerHp) || monsterHp <= 0)
					break;
				if (actor.hp <= 0) {
					continue;
				}

				int attackCount = Math.max(1, actor.speed / 50 + random.nextInt(3) - 1);

				if (actor.isPlayer) {

					log.append("\n").append(actor.name).append("의 ").append(attackCount).append("회 공격!");
					for (int i = 0; i < attackCount; i++) {
						int damage = calculateDamage(actor.attack, monster.getDefense(), actor.criticalChance, random,
							log, actor.name);
						monsterHp -= damage;

						// 스킬 구현 예정
					}
				} else {

					List<Integer> aliveIndices = new ArrayList<>();

					for (int i = 0; i < playerHp.size(); i++) {
						if (playerHp.get(i) > 0)
							aliveIndices.add(i);
					}

					if (!aliveIndices.isEmpty()) {
						int targetIndex = aliveIndices.get(random.nextInt(aliveIndices.size()));

						log.append("\n")
							.append(monster.getMonsterName())
							.append("의 ")
							.append(attackCount)
							.append("회 공격!");
						for (int i = 0; i < attackCount; i++) {
							int damage = calculateDamage(actor.attack, playerDefense.get(targetIndex),
								actor.criticalChance, random,
								log, monster.getMonsterName());
							playerHp.set(targetIndex, playerHp.get(targetIndex) - damage);
						}
					}
				}

				for (int i = 0; i < clover.size(); i++) {
					log.append("\n").append(clover.get(i).getName()).append(" ")
						.append("\n 현재 HP: ").append(playerHp.get(i)).append("/").append(clover.get(i).getMaxHp())
						.append(" 현재 MP: ").append(clover.get(i).getMp()).append("/").append(clover.get(i).getMaxMp());
				}
				log.append("\n").append(monster.getMonsterName()).append("HP: ").append(monsterHp);
			}
		}

		BattleResult battleResult = (!playerHpCheck(playerHp) && monsterHp <= 0) ? BattleResult.WIN
			: (playerHpCheck(playerHp) && monsterHp > 0) ? BattleResult.LOSE
			: BattleResult.DRAW;

		return BattleResultVo.builder()
			.battleResult(battleResult)
			.playerHp(playerHp)
			.playerMp(playerMp)
			.playerAttack(playerAttack)
			.playerDefense(playerDefense)
			.playerSpeed(playerSpeed)
			.weapon(weapons)
			.armor(armors)
			.accessory(accessories)
			.monsterAttack(monster.getAttack())
			.monsterDefense(monster.getDefense())
			.monsterSpeed(monster.getSpeed())
			.log(log.toString())
			.build();
	}

	private boolean playerHpCheck(List<Integer> playerHp) {
		for (Integer hp : playerHp) {
			if (hp > 0) {
				return false;
			}
		}
		return true;

	}

	private List<ActionEntity> buildTurnQueue(List<GameCharacter> clover, Monster monster,
		List<Integer> playerHps, List<Integer> playerMps, List<Integer> attacks, List<Integer> magics,
		List<Integer> defenses, List<Integer> speeds,
		List<Integer> crits) {

		List<ActionEntity> queue = new ArrayList<>();

		for (int i = 0; i < clover.size(); i++) {
			GameCharacter rabbit = clover.get(i);
			queue.add(new ActionEntity(rabbit.getName(), speeds.get(i), true,
				playerHps.get(i), playerMps.get(i), attacks.get(i), magics.get(i), defenses.get(i), crits.get(i),
				rabbit));
		}

		queue.add(new ActionEntity(monster.getMonsterName(), monster.getSpeed(), false,
			monster.getHp(), null, monster.getAttack(), null, monster.getDefense(), 0, null));

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
		Integer speed;
		boolean isPlayer;
		Integer hp;
		Integer mp;
		Integer attack;
		Integer magic;
		Integer defense;
		Integer criticalChance;
		GameCharacter character; // optional
	}
}
