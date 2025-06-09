package com.example.rabbithell.domain.battle.postProcess.command;

import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.clover.entity.Clover;

public interface BattleRewardCommand {

	default void execute(GameCharacter ch) {

	}

	default void execute(Clover clover) {

	}

}
