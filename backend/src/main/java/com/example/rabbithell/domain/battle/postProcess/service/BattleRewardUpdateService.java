package com.example.rabbithell.domain.battle.postProcess.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.rabbithell.domain.battle.postProcess.command.BattleRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.CashRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.ExpRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.JobPointRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.LevelUpCommand;
import com.example.rabbithell.domain.battle.postProcess.command.SkillPointRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.StatRewardCommand;
import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BattleRewardUpdateService {

	private final CharacterRepository characterRepository;
	private final CloverRepository cloverRepository;

	public void applyCharacterRewards(List<BattleRewardCommand> commands) {
		Map<Long, GameCharacter> updatedCharacters = new HashMap<>();

		for (BattleRewardCommand command : commands) {
			if (command instanceof ExpRewardCommand expCmd) {
				GameCharacter ch = expCmd.getCharacter();
				ch.updateExp(expCmd.getResultExp());
				updatedCharacters.put(ch.getId(), ch);
			} else if (command instanceof LevelUpCommand levelUpCmd) {
				GameCharacter ch = levelUpCmd.getCharacter();
				ch.updateLevel(levelUpCmd.getResultLevel());
				updatedCharacters.put(ch.getId(), ch);
			} else if (command instanceof SkillPointRewardCommand skillCmd) {
				GameCharacter ch = skillCmd.getCharacter();
				ch.updateSkillPoint(skillCmd.getUpdatedSkillPoints());
				updatedCharacters.put(ch.getId(), ch);
			} else if (command instanceof JobPointRewardCommand jobPointCmd) {
				GameCharacter ch = jobPointCmd.getCharacter();
				ch.updateJobPoint(jobPointCmd.getUpdatedJobPoints());
				updatedCharacters.put(ch.getId(), ch);
			} else if (command instanceof StatRewardCommand statRewardCmd) {
				GameCharacter ch = statRewardCmd.getCharacter();
				ch.updateStrength(statRewardCmd.getUStrength());
				ch.updateAgility(statRewardCmd.getUAgility());
				ch.updateIntelligence(statRewardCmd.getUIntelligence());
				ch.updateFocus(statRewardCmd.getUFocus());
				updatedCharacters.put(ch.getId(), ch);
			}
		}

		// Batch update
		characterRepository.saveAll(updatedCharacters.values());
	}

	public void applyCashReward(CashRewardCommand cashCommand, Clover clover) {
		clover.earnCash((int)cashCommand.getEarnedCash());
		cloverRepository.save(clover);
	}
}
