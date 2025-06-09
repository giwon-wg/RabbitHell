package com.example.rabbithell.domain.battle.postProcess.command;

import java.util.ArrayList;
import java.util.List;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.clover.entity.Clover;

public class BattleRewardExecutor {
	private final List<BattleRewardCommand> commands = new ArrayList<>();

	public void addCommand(BattleRewardCommand command) {
		commands.add(command);
	}

	public void characterExecuteAll(GameCharacter ch) {
		for (BattleRewardCommand command : commands) {
			command.execute(ch);
		}
	}

	public void cloverExecuteAll(Clover clover) {
		for (BattleRewardCommand command : commands) {
			command.execute(clover);
		}
	}

}
