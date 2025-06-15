package com.example.rabbithell.domain.deck.dto.response;

import java.util.List;

import com.example.rabbithell.domain.deck.dto.DeckRedisDto;
import com.example.rabbithell.domain.deck.dto.PawCardEffectDto;

public record BatchActivePawCardResponse(
	List<DeckRedisDto> deckRedisDtoList,
	PawCardEffectDto pawCardEffectDto
) {

	public static BatchActivePawCardResponse from(
		List<DeckRedisDto> deckRedisDtoList,
		PawCardEffectDto pawCardEffectDto
	) {
		return new BatchActivePawCardResponse(deckRedisDtoList, pawCardEffectDto);
	}
}
