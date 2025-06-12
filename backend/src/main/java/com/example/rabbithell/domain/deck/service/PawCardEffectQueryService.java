package com.example.rabbithell.domain.deck.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.deck.dto.EffectDetailDto;
import com.example.rabbithell.domain.deck.dto.PawCardEffectDto;
import com.example.rabbithell.domain.deck.entity.EffectDetail;
import com.example.rabbithell.domain.deck.entity.PawCardEffect;
import com.example.rabbithell.domain.deck.repository.PawCardEffectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PawCardEffectQueryService {

	private final PawCardEffectRepository pawCardEffectRepository;

	@Transactional(readOnly = true)
	public PawCardEffectDto getEffectOnly(Long cloverId) {
		PawCardEffect effect = pawCardEffectRepository.findByCloverIdWithDetails(cloverId);
		List<EffectDetail> details = effect.getDetails();

		return PawCardEffectDto.from(
			effect,
			details.stream().map(EffectDetailDto::fromEntity).toList()
		);
	}
}
