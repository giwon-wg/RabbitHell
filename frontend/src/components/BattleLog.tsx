import React from 'react';
import styles from '../styles/BattleLog.module.css';

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

interface BattleLogProps {
    turns: TurnLog[];
    characterNames: string[];
    monsterName: string;
}

const BattleLog = ({turns, characterNames, monsterName}: BattleLogProps) => {

    const getLineClasses = (lines: string[]) => {
        let lastActor: 'character' | 'monster' | null = null;

        return lines.map((line) => {
            if (line.includes(monsterName)) {
                lastActor = 'monster';
                return {line, className: styles.right};
            }
            if (characterNames.some(name => line.includes(name))) {
                lastActor = 'character';
                return {line, className: styles.left};
            }
            if (line.includes("크리티컬!!!")) {
                const baseClass = lastActor === 'character' ? styles.left : styles.right;
                return {line, className: `${baseClass} ${styles.critical}`};
            }
            return {line, className: styles.system};
        });
    };

    return (
        <div className={styles.battleLogContainer}>
            <h4>전투 로그</h4>
            <div className={styles.logBox}>
                {turns.map((turn) => (
                    <div key={turn.turnNumber} className={styles.turnBlock}>
                        <h5 className={styles.turnHeader}>⚔️ Turn {turn.turnNumber}</h5>

                        <div className={styles.statusBars}>
                            <div className={styles.leftStatusGroup}>
                                {turn.statuses
                                    .filter((status) => characterNames.includes(status.name))
                                    .map((status) => (
                                        <div key={status.name} className={styles.leftStatusItem}>
                                            <p className={styles.characterName}>{status.name}</p>
                                            <div className={styles.hpBarContainer}>
                                                <div
                                                    className={styles.hpBar}
                                                    style={{width: `${(status.currentHP / status.maxHP) * 100}%`}}
                                                ></div>
                                            </div>
                                            <div className={styles.mpBarContainer}>
                                                <div
                                                    className={styles.mpBar}
                                                    style={{width: `${(status.currentMP / status.maxMP) * 100}%`}}
                                                ></div>
                                            </div>
                                        </div>
                                    ))}
                            </div>

                            <div className={styles.rightStatusGroup}>
                                {turn.statuses
                                    .filter((status) => status.name === monsterName)
                                    .map((status) => (
                                        <div key={status.name} className={styles.rightStatusItem}>
                                            <p className={styles.characterName}>{status.name}</p>
                                            <div className={styles.hpBarContainer}>
                                                <div
                                                    className={styles.hpBar}
                                                    style={{width: `${(status.currentHP / status.maxHP) * 100}%`}}
                                                ></div>
                                            </div>
                                            <div className={styles.mpBarContainer}>
                                                <div
                                                    className={styles.mpBar}
                                                    style={{width: `${(status.currentMP / status.maxMP) * 100}%`}}
                                                ></div>
                                            </div>
                                        </div>
                                    ))}
                            </div>
                        </div>

                        {getLineClasses(turn.lines).map(({line, className}, index) => (
                            <p key={index} className={`${styles.logLine} ${className}`}>
                                {line}
                            </p>
                        ))}


                    </div>
                ))}
            </div>
        </div>
    );
};

export default BattleLog;
