package com.example.rabbithell.domain.battle.postProcess.strategy;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.rabbithell.domain.battle.enums.BattleResult;

@Component
public class BattleRewardStrategyFactory {

	private final Map<BattleResult, BattleRewardStrategy> strategyMap;

	public BattleRewardStrategyFactory(WinRewardStrategy winStrategy
		/*, LoseRewardStrategy loseStrategy, DrawRewardStrategy drawStrategy */) {

		this.strategyMap = new HashMap<>();
		strategyMap.put(BattleResult.WIN, winStrategy);
		// strategyMap.put(BattleResult.LOSE, loseStrategy);
		// strategyMap.put(BattleResult.DRAW, drawStrategy);
	}

	public BattleRewardStrategy getStrategy(BattleResult result) {
		return strategyMap.getOrDefault(result, strategyMap.get(BattleResult.WIN));
	}
}
