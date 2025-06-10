import React, {useEffect, useState} from 'react';

type BattleField = {
	code: string;
	name: string;
	isRare: boolean;
};
type Job = {
	name: string;
};

type ItemDto = {
	id: string;
	name: string;
};

type EarnedItemDto = {
	id: string;
	name: string;
};

type BattleResult = 'WIN' | 'LOSE' | 'DRAW';

type BattleResultResponse = {
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
	weapon: ItemDto[];
	armor: ItemDto[];
	accessory: ItemDto[];
	playerAttack: number[];
	playerDefense: number[];
	playerSpeed: number[];
	monsterAttack: number;
	monsterDefense: number;
	monsterSpeed: number;
	battleResult: BattleResult;
	battleLog: string;
	earnedItems: EarnedItemDto[];
	usedPotionHp: number;
	usedPotionMp: number;
};


const BattlePage = () => {
	const [battleFields, setBattleFields] = useState<BattleField[]>([]);
	const [loading, setLoading] = useState(true);
	const [battleResult, setBattleResult] = useState<BattleResultResponse | null>(null);

	useEffect(() => {
		loadBattleFields();
	}, []);

	const loadBattleFields = async () => {
		try {
			const token = localStorage.getItem("accessToken");
			const res = await fetch('http://localhost:8080/battles', {
				headers: {Authorization: `Bearer ${token}`},
			});
			const data = await res.json();
			setBattleFields(data.result.unlockedRareMaps);
		} catch {
			alert("전투 필드를 불러오는 데 실패했습니다.");
		} finally {
			setLoading(false);
		}
	};

	const startBattle = (fieldCode: string) => {
		const token = localStorage.getItem("accessToken");

		fetch('http://localhost:8080/battles', {
			method: 'PATCH',
			headers: {
				'Content-Type': 'application/json',
				Authorization: `Bearer ${token}`,
			},
			body: JSON.stringify({
				battleFieldType: fieldCode,
			}),
		})
			.then(res => res.json())
			.then(data => {
				setBattleResult(data.result);
				loadBattleFields();
			})
			.catch(err => {
				alert(err.message);
			});
	};

	return (
		<div style={{padding: '24px'}}>
			<h1>전투 페이지</h1>
			{loading ? (
				<p>로딩 중...</p>
			) : (
				<>
					<p>전투 가능한 필드 목록:</p>
					<div style={{display: 'flex', gap: '8px', flexWrap: 'wrap'}}>
						{battleFields.map((field) => (
							<button key={field.code} onClick={() => startBattle(field.code)}>
								{field.name}
							</button>
						))}
					</div>
					<div>
						{battleResult && (
							<div style={{
								marginTop: '16px',
								padding: '12px',
								border: '1px solid #ccc',
								borderRadius: '8px'
							}}>
								<h3>전투 결과</h3>
								<p>클로버 ID: {battleResult.cloverId}</p>
								<p>스태미너: {battleResult.stamina}</p>
								<p>전투 결과: {battleResult.battleResult}</p>
								<p>전투 로그:</p>
								<pre style={{background: '#eee', padding: '8px'}}>{battleResult.battleLog}</pre>

								<p>얻은 경험치: {battleResult.earnedExp}</p>
								<p>획득한 현금: {battleResult.lostOrEarnedCash}</p>
								<p>현재 총 현금: {battleResult.totalCash}</p>

								<p>획득 아이템:</p>
								<ul>
									{battleResult.earnedItems.map(item => (
										<li key={item.id}>{item.name} </li>
									))}
								</ul>

								{/* 필요한 다른 필드도 원하는 형태로 추가 가능 */}
							</div>
						)}
					</div>
				</>
			)}
		</div>
	);
};

export default BattlePage;
