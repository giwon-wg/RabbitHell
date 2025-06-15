// src/types.ts
export type BattleField = {
	code: string;
	name: string;
	isRare: boolean;
};

export type Job = {
	name: string;
};

export type ItemDto = {
	id: string;
	name: string;
};

export type EarnedItemDto = {
	id: string;
	name: string;
};

export type BattleResult = 'WIN' | 'LOSE' | 'DRAW';

export type BattleResultResponse = {
	cloverId: number;
	stamina: number;
	characterIds: number[];
	level: number[];
	earnedExp: number;
	totalExp: number[];
	levelUpAmounts: number[];
	lostOrEarnedCash: number;
	totalCash: number;
	jobs: Job[];
	earnedSkillPoint: number;
	totalSkillPoints: number[];
	increasedStats: number[][];
	battleFieldTypes: string[];
	characterNames: string[];
	weapon: ItemDto[];
	armor: ItemDto[];
	accessory: ItemDto[];
	playerHp: number[];
	maxHp: number[];
	playerMp: number[];
	maxMp: number[];
	playerAttack: number[];
	playerDefense: number[];
	playerSpeed: number[];
	monsterName: string;
	monsterHp: number;
	monsterMaxHp: number;
	monsterAttack: number;
	monsterDefense: number;
	monsterSpeed: number;
	battleResult: BattleResult;
	battleLog: string;
	earnedItems: EarnedItemDto[];
	usedPotionHp: number;
	usedPotionMp: number;
};
