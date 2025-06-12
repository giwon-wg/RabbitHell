interface CharacterStatus {
	name: string;
	currentHP: number;
	maxHP: number;
	currentMP: number;
	maxMP: number;
}

interface TurnLog {
	turnNumber: number;
	lines: string[];  // 기존 로그
	statuses: CharacterStatus[];  // 추가된 상태 정보
}

export const parseBattleLog = (log: string): TurnLog[] => {
	const lines = log.split('\n');
	const turnLogs: TurnLog[] = [];
	let currentTurn: TurnLog | null = null;

	lines.forEach((line) => {
		const turnMatch = line.match(/^\[Turn (\d+)\]/);
		if (turnMatch) {
			if (currentTurn) turnLogs.push(currentTurn);
			currentTurn = {turnNumber: parseInt(turnMatch[1], 10), lines: [], statuses: []};
		} else if (line.match(/HP:/)) {
			// HP/MP 파싱
			const statusMatch = line.match(/(.*?)(현재 )?HP:\s*(\d+)\/(\d+)\s*(현재 MP:\s*(\d+)\/(\d+))?/);
			if (statusMatch && currentTurn) {
				currentTurn.statuses.push({
					name: statusMatch[1].trim(),
					currentHP: parseInt(statusMatch[3], 10),
					maxHP: parseInt(statusMatch[4], 10),
					currentMP: statusMatch[6] ? parseInt(statusMatch[6], 10) : 0,
					maxMP: statusMatch[7] ? parseInt(statusMatch[7], 10) : 0,
				});
			}
		} else {
			if (currentTurn) currentTurn.lines.push(line);
		}
	});

	if (currentTurn) turnLogs.push(currentTurn);
	return turnLogs;
};
