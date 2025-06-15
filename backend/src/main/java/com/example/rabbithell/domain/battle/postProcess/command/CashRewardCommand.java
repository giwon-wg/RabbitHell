package com.example.rabbithell.domain.battle.postProcess.command;

import com.example.rabbithell.domain.clover.entity.Clover;

import lombok.Getter;

@Getter
public class CashRewardCommand implements BattleRewardCommand {
	private final long earnedCash;
	private long resultCash;

	public CashRewardCommand(long earnedCash) {
		this.earnedCash = earnedCash;
	}

	@Override
	public void execute(Clover clover) {
		resultCash = earnedCash + clover.getCash();
		clover.earnCash((int)earnedCash);
	}
}
