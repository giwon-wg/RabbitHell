// 클로버 정보
export type CloverResponse = {
	id: number;
	name: string;
	stamina: number;
	kingdomName: string;
	specieName: string;
	cash: number;
	saving: number;
};

// 캐릭터 정보 (CharacterPersonalInfoResponse 기준)
export type CharacterPersonalInfoResponse = {
	cloverId: number;
	cloverName: string;
	kingdomName: string;
	speciesName: string;
	characterName: string;
	job: string;
	level: number;
	exp: number;
	maxHp: number;
	hp: number;
	maxMp: number;
	mp: number;
	strength: number;
	agility: number;
	intelligence: number;
	focus: number;
	luck: number;
	warriorPoint: number;
	thiefPoint: number;
	wizardPoint: number;
	archerPoint: number;
	skillPoint: number;
};
