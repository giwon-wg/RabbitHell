package com.example.rabbithell.domain.battle.postProcess.strategy;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.rabbithell.domain.battle.enums.BattleResult;

@Component
public class BattleRewardStrategyFactory {
	private final Map<BattleResult, BattleRewardStrategy> strategyMap;

	public BattleRewardStrategyFactory() {
		strategyMap = new HashMap<>();
		strategyMap.put(BattleResult.WIN, new WinRewardStrategy());
		// strategyMap.put(BattleResult.LOSE, new LoseRewardStrategy());
		// strategyMap.put(BattleResult.DRAW, new DrawRewardStrategy());
	}

	public BattleRewardStrategy getStrategy(BattleResult result) {
		return strategyMap.getOrDefault(result, new DrawRewardStrategy());
	}

}
