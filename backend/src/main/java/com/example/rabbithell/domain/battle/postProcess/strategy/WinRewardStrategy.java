package com.example.rabbithell.domain.battle.postProcess.strategy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.battle.postProcess.command.BattleRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.BattleRewardCommandFactory;
import com.example.rabbithell.domain.battle.postProcess.command.BattleRewardExecutor;
import com.example.rabbithell.domain.battle.postProcess.command.ExpRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.JobPointRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.LevelUpCommand;
import com.example.rabbithell.domain.battle.postProcess.command.SkillPointRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.service.BattleRewardUpdateService;
import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.battle.vo.BattleRewardResultVo;
import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.monster.entity.Monster;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WinRewardStrategy implements BattleRewardStrategy {

	private final BattleRewardCommandFactory commandFactory;
	private final BattleRewardUpdateService updateService;

	@Override
	@Transactional
	public BattleRewardResultVo applyReward(Clover clover, List<GameCharacter> team, Monster monster,
		BattleFieldType fieldType) {

		BattleRewardExecutor executor = new BattleRewardExecutor();
		List<BattleRewardCommand> allCommands = new ArrayList<>();

		// 1. 커맨드 생성
		List<ExpRewardCommand> expCommands = commandFactory.createExpCommands(team, monster.getExp());
		List<LevelUpCommand> levelCommands = commandFactory.createLevelUpCommands(team, expCommands);
		List<SkillPointRewardCommand> skillCommands = commandFactory.createSkillPointCommands(team,
			fieldType.getSkillPoints());
		List<JobPointRewardCommand> jobCommands = commandFactory.createJobPointCommands(team,
			fieldType.getSkillPoints());
		// CashRewardCommand cashCommand = commandFactory.createCashCommand(clover, 1000L);

		// 2. 실행 순서대로 등록
		expCommands.forEach(cmd -> {
			executor.addCommand(cmd);
			allCommands.add(cmd);
		});
		executor.executeAll(); // EXP 먼저 처리

		levelCommands.forEach(cmd -> {
			executor.addCommand(cmd);
			allCommands.add(cmd);
		});
		skillCommands.forEach(cmd -> {
			executor.addCommand(cmd);
			allCommands.add(cmd);
		});
		jobCommands.forEach(cmd -> {
			executor.addCommand(cmd);
			allCommands.add(cmd);
		});

		// executor.addCommand(cashCommand);
		// allCommands.add(cashCommand);

		executor.executeAll(); // 나머지 실행

		// 3. 결과 수집
		List<Integer> updatedExps = new ArrayList<>();
		List<Integer> updatedLevels = new ArrayList<>();
		List<Integer> levelUps = new ArrayList<>();
		List<Integer> updatedSkillPoints = new ArrayList<>();
		List<Integer> updatedJobSkillPoints = new ArrayList<>();

		for (int i = 0; i < team.size(); i++) {
			updatedExps.add(expCommands.get(i).getResultExp());
			updatedLevels.add(levelCommands.get(i).getResultLevel());
			levelUps.add(levelCommands.get(i).getLevelUpAmount());
			updatedSkillPoints.add(skillCommands.get(i).getUpdatedSkillPoints());
			updatedJobSkillPoints.add(jobCommands.get(i).getUpdatedJobPoints());
		}

		// 4. 결과 업데이트
		updateService.applyCharacterRewards(allCommands);

		return new BattleRewardResultVo(
			monster.getExp(),
			fieldType.getSkillPoints(),
			1000L,
			0L, // cashCommand.getResultCash(),
			updatedExps,
			updatedLevels,
			levelUps,
			updatedSkillPoints,
			updatedJobSkillPoints
		);
	}

}
