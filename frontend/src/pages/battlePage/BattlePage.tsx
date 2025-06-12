import React, {useEffect, useState} from 'react';
import styles from '../../styles/BattlePage.module.css';
import {BattleField, BattleResultResponse} from '../../types/battleTypes';
import BattleLog from "../../components/BattleLog";
import {parseBattleLog} from "../../util/ParseBattleLog";

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
			<h4>전투 페이지</h4>
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
							<p>골드: {battleResult.totalCash} ({battleResult.lostOrEarnedCash})</p>


							{battleResult.earnedItems.length > 0 && (
								<>
									<ul>
										{battleResult.earnedItems.map(item => (
											<p key={item.id}>{item.name} 획득!</p>
										))}
									</ul>
								</>
							)}


							<div className={styles.battleContainer}>
								<div className={styles["charactersContainer"]}>
									{battleResult.characterNames.map((name, index) => (
										<div key={index} className={styles.character}>
											<h4>{name}</h4>
											<p>레벨: {battleResult.level[index]} </p>
											<p>경험치: {battleResult.totalExp[index]} (+{battleResult.earnedExp}) </p>
											<p>무기: {battleResult.weapon[index]?.name || '없음'}</p>
											<p>방어구: {battleResult.armor[index]?.name || '없음'}</p>
											<p>장신구: {battleResult.accessory[index]?.name || '없음'}</p>
											<p>공격력: {battleResult.playerAttack[index]}</p>
											<p>방어력: {battleResult.playerDefense[index]}</p>
											<p>속도: {battleResult.playerSpeed[index]}</p>
											<p>SP:
												{battleResult.totalSkillPoints[index]} (+{battleResult.earnedSkillPoint}) </p>

											{battleResult.levelUpAmounts[index] !== 0 && (
												<div className={styles.levelUpBox}>
													<p className={styles.levelUpTitle}>🎉
														Level {battleResult.levelUpAmounts[index]} UP!</p>
													<div className={styles.statsGrid}>
														<div className={styles.statItem}><span
															className={styles.label}>힘</span><span
															className={styles.value}>{battleResult.increasedStats[index][0]}</span>
														</div>
														<div className={styles.statItem}><span
															className={styles.label}>지력</span><span
															className={styles.value}>{battleResult.increasedStats[index][1]}</span>
														</div>
														<div className={styles.statItem}><span
															className={styles.label}>집중력</span><span
															className={styles.value}>{battleResult.increasedStats[index][2]}</span>
														</div>
														<div className={styles.statItem}><span
															className={styles.label}>민첩</span><span
															className={styles.value}>{battleResult.increasedStats[index][3]}</span>
														</div>
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
							</div>


							{battleResult && (
								<BattleLog
									turns={parseBattleLog(battleResult.battleLog)}
									characterNames={battleResult.characterNames}
									monsterName={battleResult.monsterName}
								/>
							)}

						</div>
					)}
				</>
			)}
		</div>
	);
};

export default BattlePage;
