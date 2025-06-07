package com.example.rabbithell.domain.battle.postProcess.command;

import java.util.Random;

import com.example.rabbithell.domain.character.entity.GameCharacter;

import lombok.Getter;

@Getter
public class StatRewardCommand implements BattleRewardCommand {
	private final GameCharacter character;
	private final int levelUps;
	private final int pStrength, pIntelligence, pFocus, pAgility;    // 레벨 업 이전
	private int uStrength, uIntelligence, uFocus, uAgility;            // 레벨 업 후
	private int iStrength, iIntelligence, iFocus, iAgility;            // 증가된 스텟

	public StatRewardCommand(GameCharacter character, int levelUps) {
		this.character = character;
		this.pStrength = character.getStrength();
		this.pIntelligence = character.getIntelligence();
		this.pFocus = character.getFocus();
		this.pAgility = character.getAgility();
		this.levelUps = levelUps;
		this.iStrength = 0;
		this.iIntelligence = 0;
		this.iFocus = 0;
		this.iAgility = 0;
	}

	@Override
	public void execute() {
		uStrength = pStrength;
		uIntelligence = pIntelligence;
		uFocus = pFocus;
		uAgility = pAgility;
		for (int i = 0; i < levelUps; i++) {
			uStrength += getRandomValue();
			uIntelligence += getRandomValue();
			uFocus += getRandomValue();
			uAgility += getRandomValue();
		}
		iStrength = uStrength - pStrength;
		iIntelligence = uIntelligence - pIntelligence;
		iFocus = uFocus - pFocus;
		iAgility = uAgility - pAgility;
	}

	public int getRandomValue() {
		Random random = new Random();
		int rand = random.nextInt(100);
		if (rand < 40) {
			return 0;
		} else if (rand < 70) {
			return 1;
		} else if (rand < 90) {
			return 2;
		} else {
			return 3;
		}
	}
}
