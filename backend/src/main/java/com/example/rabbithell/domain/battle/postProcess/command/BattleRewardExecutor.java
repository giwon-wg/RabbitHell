package com.example.rabbithell.domain.battle.postProcess.command;

import java.util.ArrayList;
import java.util.List;

public class BattleRewardExecutor {
	private final List<BattleRewardCommand> commands = new ArrayList<>();

	public void addCommand(BattleRewardCommand command) {
		commands.add(command);
	}

	public void executeAll() {
		for (BattleRewardCommand command : commands) {
			command.execute();
		}
	}
}
