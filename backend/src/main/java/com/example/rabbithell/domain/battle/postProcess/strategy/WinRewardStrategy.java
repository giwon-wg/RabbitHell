package com.example.rabbithell.domain.battle.postProcess.strategy;

import java.util.ArrayList;
import java.util.List;

import com.example.rabbithell.domain.monster.enums.Rating;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.battle.postProcess.command.BattleRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.BattleRewardCommandFactory;
import com.example.rabbithell.domain.battle.postProcess.command.BattleRewardExecutor;
import com.example.rabbithell.domain.battle.postProcess.command.CashRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.ExpRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.JobPointRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.LevelUpCommand;
import com.example.rabbithell.domain.battle.postProcess.command.SkillPointRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.StatRewardCommand;
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

		// 2. 실행 순서대로 등록
		expCommands.forEach(cmd -> {
			executor.addCommand(cmd);
			allCommands.add(cmd);
		});
		executor.executeAll(); // EXP 먼저 처리

		int earnedSkillPoint = fieldType.getSkillPoints();
		switch (monster.getRating()) {
			case RARE -> earnedSkillPoint *= 10;
			case ELITE -> earnedSkillPoint *= 20;
			case MINI_BOSS -> earnedSkillPoint *= 30;
			case BOSS -> earnedSkillPoint *= 100;
			case SPECIAL -> earnedSkillPoint *= 7;
		}
		List<LevelUpCommand> levelCommands = commandFactory.createLevelUpCommands(team, expCommands);
		List<SkillPointRewardCommand> skillCommands = commandFactory.createSkillPointCommands(team,
			earnedSkillPoint);
		List<JobPointRewardCommand> jobCommands = commandFactory.createJobPointCommands(team,
			earnedSkillPoint);
		CashRewardCommand cashCommand = commandFactory.createCashCommand(clover, 1000L);

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

		executor.addCommand(cashCommand);
		allCommands.add(cashCommand);

		executor.executeAll(); // 나머지 실행

		// 3. 결과 수집
		List<Integer> updatedExps = new ArrayList<>();
		List<Integer> updatedLevels = new ArrayList<>();
		List<Integer> levelUps = new ArrayList<>();
		List<Integer> updatedSkillPoints = new ArrayList<>();
		List<Integer> updatedJobSkillPoints = new ArrayList<>();
		Long updatedCash;

		for (int i = 0; i < team.size(); i++) {
			updatedExps.add(expCommands.get(i).getResultExp());
			updatedLevels.add(levelCommands.get(i).getResultLevel());
			levelUps.add(levelCommands.get(i).getLevelUpAmount());
			updatedSkillPoints.add(skillCommands.get(i).getUpdatedSkillPoints());
			updatedJobSkillPoints.add(jobCommands.get(i).getUpdatedJobPoints());
		}
		updatedCash = cashCommand.getResultCash();

		List<List<Integer>> increasedStats = new ArrayList<>();
		List<StatRewardCommand> statCommands = new ArrayList<>();


		List<StatRewardCommand> statRewardCommands = commandFactory.createStatRewardCommand(team, levelUps);
		statRewardCommands.forEach(cmd -> {
			executor.addCommand(cmd);
			allCommands.add(cmd);
			statCommands.add(cmd);
		});


		executor.executeAll();

		for (int i = 0; i < team.size(); i++) {
			List<Integer> increasedStat = new ArrayList<>();
			StatRewardCommand cmd = statCommands.get(i);

			if (cmd != null) {
				increasedStat.add(cmd.getIStrength());
				increasedStat.add(cmd.getIIntelligence());
				increasedStat.add(cmd.getIFocus());
				increasedStat.add(cmd.getIAgility());
			} else {
				increasedStat.add(0); // 혹은 null
				increasedStat.add(0);
				increasedStat.add(0);
				increasedStat.add(0);
			}

			increasedStats.add(increasedStat);
		}

		// 4. 결과 업데이트
		updateService.applyCharacterRewards(allCommands);
		updateService.applyCashReward(cashCommand, clover);

		return new BattleRewardResultVo(
			monster.getExp(),
			fieldType.getSkillPoints(),
			1000L,
			updatedCash,
			updatedExps,
			updatedLevels,
			levelUps,
			updatedSkillPoints,
			updatedJobSkillPoints,
			increasedStats
		);
	}

	// public void updateExp(int value) {
	// 	this.exp = value;
	// }
	//
	// public void updateSkillPoint(int value) {
	// 	this.skillPoint = value;
	// }
	//
	// public void updateJobPoint(int value) {
	// 	if (this.job.getJobCategory() == JobCategory.WARRIOR) {
	// 		this.warriorPoint = value;
	// 	} else if (this.job.getJobCategory() == JobCategory.THIEF) {
	// 		this.thiefPoint = value;
	// 	} else if (this.job.getJobCategory() == JobCategory.ARCHER) {
	// 		this.archerPoint = value;
	// 	} else if (this.job.getJobCategory() == JobCategory.WIZARD) {
	// 		this.wizardPoint = value;
	// 	} else if (this.job.getJobCategory() == JobCategory.INCOMPETENT) {
	// 		this.incompetentPoint = value;
	// 	}
	// }
	//
	// public void updateLevel(int resultLevel) {
	// 	this.level = resultLevel;
	// }

}
