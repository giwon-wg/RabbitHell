package com.example.rabbithell.domain.battle.postProcess.strategy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.battle.postProcess.command.BattleRewardExecutor;
import com.example.rabbithell.domain.battle.postProcess.command.CashRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.ExpRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.ItemDropCommand;
import com.example.rabbithell.domain.battle.postProcess.command.JobPointRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.RareMapCommand;
import com.example.rabbithell.domain.battle.postProcess.command.SkillPointRewardCommand;
import com.example.rabbithell.domain.battle.postProcess.command.StatRewardCommand;
import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.battle.vo.BattleRewardResultVo;
import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.repository.InventoryItemRepository;
import com.example.rabbithell.domain.inventory.repository.InventoryRepository;
import com.example.rabbithell.domain.item.entity.Item;
import com.example.rabbithell.domain.monster.entity.DropRate;
import com.example.rabbithell.domain.monster.entity.Monster;
import com.example.rabbithell.domain.monster.repository.DropRateRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WinRewardStrategy implements BattleRewardStrategy {

	private final CloverRepository cloverRepository;
	private final DropRateRepository dropRateRepository;
	private final CharacterRepository characterRepository;
	private final InventoryRepository inventoryRepository;
	private final InventoryItemRepository inventoryItemRepository;

	@Override
	@Transactional
	public BattleRewardResultVo applyReward(Clover clover, List<GameCharacter> team, Monster monster,
		BattleFieldType fieldType) {

		Clover updatedClover = cloverRepository.findByIdOrElseThrow(clover.getId());
		updatedClover.clearUnlockedRareMaps();

		List<GameCharacter> updatedTeam = new ArrayList<>();

		for (GameCharacter character : team) {
			updatedTeam.add(characterRepository.findByIdOrElseThrow(character.getId()));
		}

		int earnedSkillPoint = fieldType.getSkillPoints();
		switch (monster.getRating()) {
			case RARE -> earnedSkillPoint *= 10;
			case ELITE -> earnedSkillPoint *= 20;
			case MINI_BOSS -> earnedSkillPoint *= 30;
			case BOSS -> earnedSkillPoint *= 100;
			case SPECIAL -> earnedSkillPoint *= 7;
		}

		List<Integer> totalExps = new ArrayList<>();
		List<Integer> levels = new ArrayList<>();
		List<Integer> levelUpAmounts = new ArrayList<>();
		List<Integer> totalSkillPoints = new ArrayList<>();
		List<List<Integer>> increasedStats = new ArrayList<>();

		// 1. 커맨드 생성

		BattleRewardExecutor cloverExecutor = new BattleRewardExecutor();

		for (GameCharacter ch : updatedTeam) {

			BattleRewardExecutor characterExecutor = new BattleRewardExecutor();

			ExpRewardCommand expRewardCommand = new ExpRewardCommand(monster.getExp());
			expRewardCommand.execute(ch);

			SkillPointRewardCommand skillPointRewardCommand = new SkillPointRewardCommand(earnedSkillPoint);
			characterExecutor.addCommand(skillPointRewardCommand);

			JobPointRewardCommand jobPointRewardCommand = new JobPointRewardCommand(earnedSkillPoint);
			characterExecutor.addCommand(jobPointRewardCommand);

			StatRewardCommand statRewardCommand = new StatRewardCommand(expRewardCommand.getLevelUpAmount());
			characterExecutor.addCommand(statRewardCommand);

			characterExecutor.characterExecuteAll(ch);

			totalExps.add(expRewardCommand.getUpdatedExp());
			levels.add(expRewardCommand.getUpdatedLevel());
			levelUpAmounts.add(expRewardCommand.getLevelUpAmount());
			totalSkillPoints.add(skillPointRewardCommand.getUpdatedSkillPoints());

			List<Integer> increasedStat = new ArrayList<>();
			increasedStat.add(statRewardCommand.getIStrength());
			increasedStat.add(statRewardCommand.getIIntelligence());
			increasedStat.add(statRewardCommand.getIFocus());
			increasedStat.add(statRewardCommand.getIAgility());
			increasedStats.add(increasedStat);

		}

		CashRewardCommand cashRewardCommand = new CashRewardCommand(1000L);
		cloverExecutor.addCommand(cashRewardCommand);

		RareMapCommand rareMapCommand = new RareMapCommand(fieldType);
		cloverExecutor.addCommand(rareMapCommand);

		List<DropRate> dropRates = dropRateRepository.findByMonster(monster);
		ItemDropCommand itemDropCommand = new ItemDropCommand(dropRates);
		cloverExecutor.addCommand(itemDropCommand);

		cloverExecutor.cloverExecuteAll(updatedClover);

		List<Item> items = itemDropCommand.getItems();

		Inventory inventory = inventoryRepository.findByCloverOrElseThrow(clover);
		if (items != null) {
			for (Item item : items) {
				InventoryItem inventoryItem = new InventoryItem(inventory, item);
				inventoryItemRepository.save(inventoryItem);
			}
		}

		characterRepository.saveAll(updatedTeam);
		cloverRepository.save(updatedClover);

		return new BattleRewardResultVo(
			monster.getExp(),
			fieldType.getSkillPoints(),
			1000L,
			updatedClover.getCash(),
			totalExps,
			levels,
			levelUpAmounts,
			totalSkillPoints,
			increasedStats,
			items,
			updatedClover.getUnlockedRareMaps()
		);
	}

}
