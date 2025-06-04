package com.example.rabbithell.domain.deck.repository;

import static com.example.rabbithell.domain.deck.exception.code.DeckExceptionCode.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.rabbithell.domain.clover.entity.QClover;
import com.example.rabbithell.domain.deck.dto.request.DeckCond;
import com.example.rabbithell.domain.deck.entity.Deck;
import com.example.rabbithell.domain.deck.entity.QDeck;
import com.example.rabbithell.domain.deck.enums.DeckSortBy;
import com.example.rabbithell.domain.deck.enums.RabbitHellSortDirection;
import com.example.rabbithell.domain.deck.exception.DeckException;
import com.example.rabbithell.domain.pawcard.entity.QPawCard;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DeckQueryRepositoryImpl implements DeckQueryRepository {

	private final JPAQueryFactory queryFactory;
	private final QPawCard pawCard = QPawCard.pawCard;
	private final QDeck deck = QDeck.deck;
	private final QClover clover = QClover.clover;

	@Override
	public List<Deck> findAllByCondition(Long cloverId, DeckCond cond, Pageable pageable) {
		return queryFactory
			.selectFrom(deck)
			.join(deck.pawCard, pawCard).fetchJoin()
			.join(deck.clover, clover).fetchJoin()
			.where(
				deck.clover.id.eq(cloverId),
				buildSlotCond(cond.equippedOnly())
			)
			.orderBy(resolveSortNullable(cond.sortBy(), cond.sortDirection()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	@Override
	public long countByCondition(Long cloverId, DeckCond cond) {
		return Optional.ofNullable(
			queryFactory
				.select(deck.count())
				.from(deck)
				.where(
					deck.clover.id.eq(cloverId),
					buildSlotCond(cond.equippedOnly())
				)
				.fetchOne()
		).orElse(0L);
	}

	public Deck findByIdAndCloverIdOrElseThrow(Long deckId, Long cloverId) {
		return Optional.ofNullable(queryFactory
				.selectFrom(deck)
				.where(
					deck.id.eq(deckId),
					deck.clover.id.eq(cloverId)
				)
				.fetchOne())
			.orElseThrow(() -> new DeckException(DECK_NOT_FOUND));
	}

	private BooleanExpression buildSlotCond(Boolean equippedOnly) {
		if (equippedOnly == null)
			return null;
		return equippedOnly ? deck.pawCardSlot.isNotNull() : deck.pawCardSlot.isNull();
	}

	private OrderSpecifier<?> resolveSortNullable(
		DeckSortBy sortBy,
		RabbitHellSortDirection direction
	) {
		DeckSortBy safeSortBy = sortBy != null ? sortBy : DeckSortBy.CREATED_AT;
		RabbitHellSortDirection safeDirection = direction != null ? direction : RabbitHellSortDirection.DESC;

		return safeSortBy.toOrderSpecifier(safeDirection.isAscending());
	}
}
