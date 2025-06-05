package com.example.rabbithell.domain.battle.postProcess.strategy;

import java.util.List;

import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.battle.vo.BattleRewardResultVo;
import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.monster.entity.Monster;

public interface BattleRewardStrategy {
	BattleRewardResultVo applyReward(Clover clover, List<GameCharacter> team, Monster monster,
		BattleFieldType fieldType);
}
