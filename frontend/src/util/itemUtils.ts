// 희귀도 테두리 색상
export const rarityBorderColors: Record<string, string> = {
	COMMON: '#999',
	RARE: '#3498db',
	UNIQUE: '#9b59b6',
	LEGENDARY: '#e67e22',
	MYTH: '#e74c3c',
};

// itemType → 대분류
export const typeGroups: Record<string, '무기' | '방어구' | '악세사리' | '소모품' | '기타'> = {
	SWORD: '무기',
	BOW: '무기',
	DAGGER: '무기',
	WAND: '무기',
	SHIELD: '방어구',
	ARMOR: '방어구',
	ACCESSORY: '악세사리',
	HP: '소모품',
	MP: '소모품',
	ETC: '기타',
};

// itemType → 이미지 폴더
export const mapSlotToFolder = (itemType: string): string => {
	const normalized = itemType.toUpperCase();
	switch (normalized) {
		case 'SWORD':
		case 'BOW':
		case 'DAGGER':
		case 'WAND':
			return 'weapon';
		case 'ARMOR':
		case 'SHIELD':
			return 'armor';
		case 'ACCESSORY':
			return 'earring';
		default:
			return 'etc';
	}
};
