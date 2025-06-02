package com.example.rabbithell.domain.battle.dto.response;

import java.util.Set;

import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.item.entity.Item;

public record BattleResultResponse(
	Long characterId,
	int stamina,
	int level,
	int earnedExp,
	int totalExp,
	int earnedMoney,
	int totalMoney,
	int earnedCash,
	int totalCash,
	int earnedSkillPoint,
	int totalSkillPoint,
	Set<BattleFieldType> battleFieldTypes,
	Item weapon,
	Item armor,
	Item accessory,
	int playerAttack,
	int playerDefense,
	int playerSpeed,
	int monsterAttack,
	int monsterDefence,
	int monsterSpeed,
	Boolean battleResult,
	String battleLog
) {
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Long characterId;
		private int stamina;
		private int level;
		private int earnedExp;
		private int totalExp;
		private int earnedMoney;
		private int totalMoney;
		private int earnedCash;
		private int totalCash;
		private int earnedSkillPoint;
		private int totalSkillPoint;
		private Set<BattleFieldType> battleFieldTypes;
		private Item weapon;
		private Item armor;
		private Item accessory;
		private int playerAttack;
		private int playerDefense;
		private int playerSpeed;
		private int monsterAttack;
		private int monsterDefence;
		private int monsterSpeed;
		private Boolean battleResult;
		private String battleLog;

		public Builder characterId(Long characterId) {
			this.characterId = characterId;
			return this;
		}

		public Builder stamina(int stamina) {
			this.stamina = stamina;
			return this;
		}

		public Builder level(int level) {
			this.level = level;
			return this;
		}

		public Builder earnedExp(int earnedExp) {
			this.earnedExp = earnedExp;
			return this;
		}

		public Builder totalExp(int totalExp) {
			this.totalExp = totalExp;
			return this;
		}

		public Builder earnedMoney(int earnedMoney) {
			this.earnedMoney = earnedMoney;
			return this;
		}

		public Builder totalMoney(int totalMoney) {
			this.totalMoney = totalMoney;
			return this;
		}

		public Builder earnedCash(int earnedCash) {
			this.earnedCash = earnedCash;
			return this;
		}

		public Builder totalCash(int totalCash) {
			this.totalCash = totalCash;
			return this;
		}

		public Builder earnedSkillPoint(int earnedSkillPoint) {
			this.earnedSkillPoint = earnedSkillPoint;
			return this;
		}

		public Builder totalSkillPoint(int totalSkillPoint) {
			this.totalSkillPoint = totalSkillPoint;
			return this;
		}

		public Builder battleFieldTypes(Set<BattleFieldType> battleFieldTypes) {
			this.battleFieldTypes = battleFieldTypes;
			return this;
		}

		public Builder weapon(Item weapon) {
			this.weapon = weapon;
			return this;
		}

		public Builder armor(Item armor) {
			this.armor = armor;
			return this;
		}

		public Builder accessory(Item accessory) {
			this.accessory = accessory;
			return this;
		}

		public Builder playerAttack(int playerAttack) {
			this.playerAttack = playerAttack;
			return this;
		}

		public Builder playerDefense(int playerDefense) {
			this.playerDefense = playerDefense;
			return this;
		}

		public Builder playerSpeed(int playerSpeed) {
			this.playerSpeed = playerSpeed;
			return this;
		}

		public Builder monsterAttack(int monsterAttack) {
			this.monsterAttack = monsterAttack;
			return this;
		}

		public Builder monsterDefence(int monsterDefence) {
			this.monsterDefence = monsterDefence;
			return this;
		}

		public Builder monsterSpeed(int monsterSpeed) {
			this.monsterSpeed = monsterSpeed;
			return this;
		}

		public Builder battleResult(Boolean battleResult) {
			this.battleResult = battleResult;
			return this;
		}

		public Builder battleLog(String battleLog) {
			this.battleLog = battleLog;
			return this;
		}

		public BattleResultResponse build() {
			return new BattleResultResponse(
				characterId,
				stamina,
				level,
				earnedExp,
				totalExp,
				earnedMoney,
				totalMoney,
				earnedCash,
				totalCash,
				earnedSkillPoint,
				totalSkillPoint,
				battleFieldTypes,
				weapon,
				armor,
				accessory,
				playerAttack,
				playerDefense,
				playerSpeed,
				monsterAttack,
				monsterDefence,
				monsterSpeed,
				battleResult,
				battleLog
			);
		}
	}
}
