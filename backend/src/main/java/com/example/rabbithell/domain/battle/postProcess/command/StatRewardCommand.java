package com.example.rabbithell.domain.battle.postProcess.command;

import java.util.Random;

import com.example.rabbithell.domain.character.entity.GameCharacter;

import lombok.Getter;

@Getter
public class StatRewardCommand implements BattleRewardCommand {

	private final int levelUpAmount;
	private int uStrength, uIntelligence, uFocus, uAgility;            // 레벨 업 후
	private int iStrength = 0, iIntelligence = 0, iFocus = 0, iAgility = 0;            // 증가된 스텟

	public StatRewardCommand(int levelUpAmount) {
		this.levelUpAmount = levelUpAmount;
	}

	@Override
	public void execute(GameCharacter ch) {
		if (levelUpAmount == 0)
			return;
		int strength = ch.getStrength();
		int intelligence = ch.getIntelligence();
		int focus = ch.getFocus();
		int agility = ch.getAgility();

		for (int i = 0; i < levelUpAmount; i++) {
			uStrength = strength + getRandomValue();
			uIntelligence = intelligence + getRandomValue();
			uFocus = focus + getRandomValue();
			uAgility = agility + getRandomValue();
		}
		iStrength = uStrength - strength;
		iIntelligence = uIntelligence - intelligence;
		iFocus = uFocus - focus;
		iAgility = uAgility - agility;

		ch.updateStrength(uStrength);
		ch.updateIntelligence(uIntelligence);
		ch.updateFocus(uFocus);
		ch.updateAgility(uAgility);
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
