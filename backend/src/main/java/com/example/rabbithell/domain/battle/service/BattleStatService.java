package com.example.rabbithell.domain.battle.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.common.effect.applier.BattleStatApplier;
import com.example.rabbithell.common.effect.applier.BattleStatApplierRegistry;
import com.example.rabbithell.common.effect.enums.StatType;
import com.example.rabbithell.domain.deck.dto.EffectDetailDto;
import com.example.rabbithell.domain.deck.dto.PawCardEffectDto;
import com.example.rabbithell.domain.deck.service.PawCardEffectRedisService;
import com.example.rabbithell.domain.util.battlelogic.PawCardBattleStatDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BattleStatService {

	private final BattleStatApplierRegistry battleStatApplierRegistry;
	private final PawCardEffectRedisService pawCardEffectRedisService;

	@Transactional(readOnly = true)
	public PawCardBattleStatDto calculatePawCard(Long cloverId) {

		PawCardEffectDto pawCardEffectDto = pawCardEffectRedisService.getEffect(cloverId);

		PawCardBattleStatDto pawCardBattleStatDto = PawCardBattleStatDto.create(); // 초기 누적 DTO

		for (EffectDetailDto effectDetailDto : pawCardEffectDto.getEffectDetailDtoList()) {
			StatType statType = effectDetailDto.getStatType();
			Integer value = effectDetailDto.getFinalEffectValue();

			BattleStatApplier applier = battleStatApplierRegistry.get(statType);
			applier.apply(value, pawCardBattleStatDto);
		}

		return pawCardBattleStatDto;
	}
}
