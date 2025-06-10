import React, {useEffect, useState} from 'react';
import styles from './BattlePage.module.css';


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
	characterNames: string[];
	weapon: ItemDto[];
	armor: ItemDto[];
	accessory: ItemDto[];
	playerAttack: number[];
	playerDefense: number[];
	playerSpeed: number[];
	monsterName: string;
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
			body: JSON.stringify({battleFieldType: fieldCode}),
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
		<div className={styles.container}>
			<h1>전투 페이지</h1>
			{loading ? (
				<p>로딩 중...</p>
			) : (
				<>
					<p>전투 가능한 필드 목록:</p>
					<div className={styles.fields}>
						{battleFields.map((field) => (
							<button
								key={field.code}
								onClick={() => startBattle(field.code)}
								className={`${styles.button} ${field.isRare ? styles.rare : ''}`}>
								{field.name}
							</button>
						))}
					</div>

					{battleResult && (
						<div className={styles.result}>
							<h3
								className={`${styles.battleResult} ${
									battleResult.battleResult === 'WIN'
										? styles.win
										: battleResult.battleResult === 'LOSE'
											? styles.lose
											: styles.draw
								}`}
							>
								{battleResult.battleResult}
							</h3>
							<div className={styles["characters-container"]}>
								{battleResult.characterNames.map((name, index) => (
									<div key={index} className={styles.character}>
										<h4>{name}</h4>
										<p>레벨: {battleResult.level[index]} </p>
										<p>무기: {battleResult.weapon[index]?.name || '없음'}</p>
										<p>방어구: {battleResult.armor[index]?.name || '없음'}</p>
										<p>장신구: {battleResult.accessory[index]?.name || '없음'}</p>
										<p>공격력: {battleResult.playerAttack[index]}</p>
										<p>방어력: {battleResult.playerDefense[index]}</p>
										<p>속도: {battleResult.playerSpeed[index]}</p>

										{battleResult.levelUpAmounts[index] !== 0 && (
											<div className={styles.levelUpBox}>
												<p className={styles.levelUpTitle}>🎉
													Level {battleResult.levelUpAmounts[index]} UP!</p>
												<div className={styles.statsGrid}>
													<p>힘: {battleResult.increasedStats[index][0]}</p>
													<p>지력: {battleResult.increasedStats[index][1]}</p>
													<p>집중력: {battleResult.increasedStats[index][2]}</p>
													<p>민첩: {battleResult.increasedStats[index][3]}</p>
												</div>
											</div>
										)}
									</div>
								))}

							</div>

							<div className={styles["monster-info"]}>
								<p>몬스터: {battleResult.monsterName}</p>
								<p>공격력: {battleResult.monsterAttack}</p>
								<p>방어력: {battleResult.monsterDefense}</p>
								<p>속도: {battleResult.monsterSpeed}</p>
							</div>

							<p>레벨업: {battleResult.levelUpAmounts}</p>
							<p>스탯 증가량: {battleResult.increasedStats}</p>
							<p>전투 로그:</p>
							<pre className={styles.battleLog}>{battleResult.battleLog}</pre>

							<p>얻은 경험치: {battleResult.earnedExp}</p>
							<p>획득한 현금: {battleResult.lostOrEarnedCash}</p>
							<p>현재 총 현금: {battleResult.totalCash}</p>

							<p>획득 아이템:</p>
							<ul>
								{battleResult.earnedItems.map(item => (
									<li key={item.id}>{item.name}</li>
								))}
							</ul>
						</div>
					)}
				</>
			)}
		</div>
	);
};

export default BattlePage;
