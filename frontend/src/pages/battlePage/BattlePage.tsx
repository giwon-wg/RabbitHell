import React, {useEffect, useState} from 'react';
import styles from '../../styles/BattlePage.module.css';
import {BattleField, BattleResultResponse} from '../../types/battleTypes';
import BattleLog from "../../components/BattleLog";
import {parseBattleLog} from "../../util/ParseBattleLog";

const BattlePage = () => {
	const [battleFields, setBattleFields] = useState<BattleField[]>([]);
	const [loading, setLoading] = useState(true);
	const [battleResult, setBattleResult] = useState<BattleResultResponse | null>(null);
	const [openedCardIndexes, setOpenedCardIndexes] = useState<number[]>([]);


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

	const toggleCard = (index: number) => {
		setOpenedCardIndexes(prev =>
			prev.includes(index) ? prev.filter(i => i !== index) : [...prev, index]
		);
	};

	return (
		<div className={styles.battleContainer}>

			<div className={styles.sideLeft}>
				<div className={styles["charactersContainer"]}>
					{battleResult?.characterNames.map((name, index) => {
						const isOpen = openedCardIndexes.includes(index);
						return (
							<div
								key={index}
								className={styles.character}
								onClick={() => toggleCard(index)}
								style={{
									transition: 'transform 0.3s ease, margin-top 0.3s ease',
									transform: isOpen ? 'translateY(450px)' : 'translateY(0)',
									marginBottom: isOpen ? '30px' : '16px'
								}}
							>
								<h4>{name}</h4>
								<div className={styles.statusBar}>
									<div className={styles.barLabel}>HP</div>
									<div className={styles.barOuter}>
										<div
											className={styles.barInner}
											style={{
												width: `${(battleResult.playerHp[index] / battleResult.maxHp[index]) * 100}%`,
												backgroundColor: 'red'
											}}
										></div>
									</div>
									<div className={styles.barText}>
										{battleResult.playerHp[index]} / {battleResult.maxHp[index]}
									</div>
								</div>

								<div className={styles.statusBar}>
									<div className={styles.barLabel}>MP</div>
									<div className={styles.barOuter}>
										<div
											className={styles.barInner}
											style={{
												width: `${(battleResult.playerMp[index] / battleResult.maxMp[index]) * 100}%`,
												backgroundColor: 'blue'
											}}
										></div>
									</div>
									<div className={styles.barText}>
										{battleResult.playerMp[index]} / {battleResult.maxMp[index]}
									</div>
								</div>

								<p>레벨: {battleResult.level[index]}</p>
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
						);
					})}


				</div>
			</div>

			<div className={styles.container}>
				{loading ? (
					<p>로딩 중...</p>
				) : (
					<>

						{battleResult && (
							<div className={styles.result}>

								<div className={styles.resultContainer}>
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
									<p className={styles.gold}>
										골드 <br/>
										{battleResult.totalCash} <br/>
										({battleResult.lostOrEarnedCash})
									</p>

									{battleResult.earnedItems.length > 0 && (
										<>

											{battleResult.earnedItems.map(item => (
												<p className={styles.item} key={item.id}>{item.name} 획득!</p>
											))}

										</>
									)}
								</div>
							</div>

						)}

						<div className={styles.fieldContainer}>
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
						</div>
					</>
				)}


				{battleResult && (
					<div className={styles.result}>
						<BattleLog
							turns={parseBattleLog(battleResult.battleLog)}
							characterNames={battleResult.characterNames}
							monsterName={battleResult.monsterName}
						/>
					</div>

				)}
			</div>

			{battleResult && (
				<div className={styles.sideRight}>
					<div className={styles["monster-info"]}>
						<p>몬스터: {battleResult.monsterName}</p>
						<div className={styles.statusBar}>
							<div className={styles.barLabel}>HP</div>
							<div className={styles.barOuter}>
								<div
									className={styles.barInner}
									style={{
										width: `${(battleResult.monsterHp / battleResult.monsterMaxHp) * 100}%`,
										backgroundColor: 'red'
									}}
								></div>
							</div>
							<div className={styles.barText}>
								{battleResult.monsterHp} / {battleResult.monsterMaxHp}
							</div>
						</div>


						<p>공격력: {battleResult.monsterAttack}</p>
						<p>방어력: {battleResult.monsterDefense}</p>
						<p>속도: {battleResult.monsterSpeed}</p>
					</div>
				</div>
			)}
		</div>
	);
};

export default BattlePage;
