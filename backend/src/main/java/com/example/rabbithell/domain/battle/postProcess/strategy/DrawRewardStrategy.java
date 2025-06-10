package com.example.rabbithell.domain.battle.postProcess.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.battle.vo.BattleRewardResultVo;
import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.monster.entity.Monster;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DrawRewardStrategy implements BattleRewardStrategy {

	private final CloverRepository cloverRepository;

	@Override
	@Transactional
	public BattleRewardResultVo applyReward(Clover clover, List<GameCharacter> team, Monster monster,
		BattleFieldType fieldType) {

		Clover updatedClover = cloverRepository.findByIdOrElseThrow(clover.getId());
		updatedClover.clearUnlockedRareMaps();

		List<Integer> zero = Arrays.asList(0, 0, 0, 0);
		List<List<Integer>> zeros = Arrays.asList(zero, zero, zero, zero);
		List<Integer> levels = team.stream().map(GameCharacter::getLevel).toList();

		cloverRepository.save(updatedClover);

		return new BattleRewardResultVo(
			0,
			0,
			0,
			clover.getCash(),
			zero,
			zero,
			levels,
			zero,
			zeros,
			new ArrayList<>(),
			updatedClover.getUnlockedRareMaps()
		);
	}
}
