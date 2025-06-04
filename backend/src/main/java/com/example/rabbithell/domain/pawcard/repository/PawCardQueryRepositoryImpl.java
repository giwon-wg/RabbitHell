package com.example.rabbithell.domain.pawcard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.rabbithell.common.effect.enums.DomainType;
import com.example.rabbithell.common.effect.enums.StatCategory;
import com.example.rabbithell.common.effect.enums.StatType;
import com.example.rabbithell.domain.pawcard.dto.request.PawCardCond;
import com.example.rabbithell.domain.pawcard.entity.PawCard;
import com.example.rabbithell.domain.pawcard.entity.QPawCard;
import com.example.rabbithell.domain.pawcard.enums.CardEmblem;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PawCardQueryRepositoryImpl implements PawCardQueryRepository {

	private final JPAQueryFactory queryFactory;
	private final QPawCard pawCard = QPawCard.pawCard;

	@Override
	public List<PawCard> findAllByCondition(PawCardCond cond, Pageable pageable) {
		return queryFactory
			.selectFrom(pawCard)
			.where(
				eqIsDeleted(cond.isDeleted()),
				eqCardNumber(cond.cardNumber()),
				eqCardEmblem(cond.cardEmblem()),
				eqStatType(cond.StatType()),
				eqStatCategory(cond.statCategory()),
				eqDomainType(cond.domainType())
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	@Override
	public long countByCondition(PawCardCond cond) {
		return Optional.ofNullable(
			queryFactory
				.select(pawCard.count())
				.from(pawCard)
				.where(
					eqIsDeleted(cond.isDeleted()),
					eqCardNumber(cond.cardNumber()),
					eqCardEmblem(cond.cardEmblem()),
					eqStatType(cond.StatType()),
					eqStatCategory(cond.statCategory()),
					eqDomainType(cond.domainType())
				)
				.fetchOne()
		).orElse(0L);
	}

	private BooleanExpression eqIsDeleted(Boolean isDeleted) {
		return isDeleted != null ? pawCard.isDeleted.eq(isDeleted) : null;
	}

	private BooleanExpression eqCardNumber(Integer cardNumber) {
		return cardNumber != null ? pawCard.cardNumber.eq(cardNumber) : null;
	}

	private BooleanExpression eqCardEmblem(CardEmblem cardEmblem) {
		return cardEmblem != null ? pawCard.cardEmblem.eq(cardEmblem) : null;
	}

	private BooleanExpression eqStatType(StatType statType) {
		return statType != null ? pawCard.statType.eq(statType) : null;
	}

	private BooleanExpression eqStatCategory(StatCategory statCategory) {
		return statCategory != null ? pawCard.statCategory.eq(statCategory) : null;
	}

	private BooleanExpression eqDomainType(DomainType domainType) {
		return domainType != null ? pawCard.domainType.eq(domainType) : null;
	}
}
