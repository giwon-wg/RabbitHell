package com.example.rabbithell.domain.battle.postProcess.strategy;

import java.util.ArrayList;
import java.util.List;

import com.example.rabbithell.domain.battle.postProcess.command.BattleRewardExecutor;
import com.example.rabbithell.domain.battle.postProcess.command.CashRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.ExpRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.JobPointRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.LevelUpCommand;
import com.example.rabbithell.domain.battle.postProcess.command.SkillPointRewardCommand;
import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.battle.vo.BattleRewardResultVo;
import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.monster.entity.Monster;

public class WinRewardStrategy implements BattleRewardStrategy {

	@Override
	public BattleRewardResultVo applyReward(Clover clover, List<GameCharacter> team, Monster monster,
		BattleFieldType fieldType) {
		int earnedExp = monster.getExp();
		int earnedSkillPoints = fieldType.getSkillPoints();
		long earnedCash = 1000L;

		List<Integer> updatedExps = new ArrayList<>();
		List<Integer> updatedLevels = new ArrayList<>();
		List<Integer> levelUps = new ArrayList<>();
		List<Integer> updatedSkillPoints = new ArrayList<>();
		List<Integer> updatedJobSkillPoints = new ArrayList<>();
		long newCash = 0L;

		BattleRewardExecutor executor = new BattleRewardExecutor();
		List<ExpRewardCommand> expCommands = new ArrayList<>();
		List<LevelUpCommand> levelCommands = new ArrayList<>();
		List<SkillPointRewardCommand> skillCommands = new ArrayList<>();
		List<JobPointRewardCommand> jobCommands = new ArrayList<>();

		for (GameCharacter character : team) {
			ExpRewardCommand expCmd = new ExpRewardCommand(character, earnedExp);
			expCommands.add(expCmd);
			executor.addCommand(expCmd);
		}

		executor.executeAll(); // EXP 먼저 계산

		for (int i = 0; i < team.size(); i++) {
			GameCharacter character = team.get(i);
			int exp = expCommands.get(i).getResultExp();

			LevelUpCommand levelCmd = new LevelUpCommand(character, exp);
			levelCommands.add(levelCmd);
			executor.addCommand(levelCmd);

			SkillPointRewardCommand skillCmd = new SkillPointRewardCommand(character, earnedSkillPoints);
			skillCommands.add(skillCmd);

			JobPointRewardCommand jobCmd = new JobPointRewardCommand(character, earnedSkillPoints);
			jobCommands.add(jobCmd);

			executor.addCommand(skillCmd);
		}

		CashRewardCommand cashCommand = new CashRewardCommand(clover.getCash(), earnedCash);
		executor.addCommand(cashCommand);

		executor.executeAll(); // 나머지 보상 처리

		for (int i = 0; i < team.size(); i++) {
			updatedExps.add(expCommands.get(i).getResultExp());
			updatedLevels.add(levelCommands.get(i).getResultLevel());
			levelUps.add(levelCommands.get(i).getLevelUpAmount());
			updatedSkillPoints.add(skillCommands.get(i).getUpdatedSkillPoints());
			updatedJobSkillPoints.add(jobCommands.get(i).getUpdatedJobPoints());
		}

		newCash = cashCommand.getResultCash();

		return new BattleRewardResultVo(
			earnedExp, earnedSkillPoints, earnedCash, newCash,
			updatedExps, updatedLevels, levelUps, updatedSkillPoints, updatedJobSkillPoints
		);
	}
}
