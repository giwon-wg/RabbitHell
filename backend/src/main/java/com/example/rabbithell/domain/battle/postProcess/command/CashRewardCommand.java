package com.example.rabbithell.domain.battle.postProcess.command;

import lombok.Getter;

public class CashRewardCommand implements BattleRewardCommand {
	private final long currentCash;
	private final long earnedCash;
	@Getter
	private long resultCash;

	public CashRewardCommand(long currentCash, long earnedCash) {
		this.currentCash = currentCash;
		this.earnedCash = earnedCash;
	}

	@Override
	public void execute() {
		resultCash = currentCash + earnedCash;

	}

}
