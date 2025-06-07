package com.example.rabbithell.domain.battle.postProcess.command;

import com.example.rabbithell.domain.clover.entity.Clover;

import lombok.Getter;

@Getter
public class CashRewardCommand implements BattleRewardCommand {
	private final Clover clover;
	private final long earnedCash;
	private long resultCash;

	public CashRewardCommand(Clover clover, long earnedCash) {
		this.clover = clover;
		this.earnedCash = earnedCash;
	}

	@Override
	public void execute() {
		resultCash = clover.getCash() + earnedCash;

	}

}
