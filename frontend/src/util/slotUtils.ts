export const getFolderBySlot = (slot: string): string => {
	switch (slot) {
		case 'HAND': return 'weapon';
		case 'BODY': return 'armor';
		case 'HEAD': return 'earring';
		default: return 'etc';
	}
};
