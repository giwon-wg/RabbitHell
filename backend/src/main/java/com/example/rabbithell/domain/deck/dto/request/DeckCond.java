package com.example.rabbithell.domain.deck.dto.request;

import com.example.rabbithell.domain.deck.enums.DeckSortBy;
import com.example.rabbithell.domain.deck.enums.RabbitHellSortDirection;

import io.swagger.v3.oas.annotations.media.Schema;

public record DeckCond(
	@Schema(description =
		"PawCard 활성화 덱만 = true "
			+ "/ PawCard 비활성화 덱만 = false "
			+ "/ 조건 없음 = null ", example = "true")
	Boolean equippedOnly,
	@Schema(description =
		"카드 넘버 기준 정렬 = CARD_NUMBER "
			+ "/ 카드 엠블럼 기준 정렬(c->d->h->s) = CARD_Emblem "
			+ "/ 카드 획득 일 기준 정렬() = CREATED_AT "
			+ "/ 비율 기준 정렬() = RATIO "
			+ "/ 조건 없음 = null(default = CREATED_AT) ", example = "CREATED_AT")
	DeckSortBy sortBy,
	@Schema(description =
		"오름차순 = ASC "
			+ "/ 내림차순 = DESC "
			+ "/ 조건 없음 = null(default = ASC) ", example = "ASC")
	RabbitHellSortDirection sortDirection) {
}
