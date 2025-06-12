package com.example.rabbithell.domain.deck.repository;

import static com.example.rabbithell.domain.deck.exception.code.DeckExceptionCode.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.rabbithell.domain.deck.entity.PawCardEffect;
import com.example.rabbithell.domain.deck.entity.QEffectDetail;
import com.example.rabbithell.domain.deck.entity.QPawCardEffect;
import com.example.rabbithell.domain.deck.exception.DeckException;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PawCardEffectQueryRepositoryImpl implements PawCardEffectQueryRepository {

	private final JPAQueryFactory queryFactory;
	private final QPawCardEffect pawCardEffect = QPawCardEffect.pawCardEffect;
	private final QEffectDetail effectDetail = QEffectDetail.effectDetail;

	@Override
	public PawCardEffect findByCloverIdWithDetails(Long cloverId) {

		return Optional.ofNullable(queryFactory
			.selectFrom(pawCardEffect)
			.join(pawCardEffect.details, effectDetail).fetchJoin()
			.where(pawCardEffect.clover.id.eq(cloverId))
			.fetchOne()).orElseThrow(() -> new DeckException(PAW_CARD_EFFECT_NOT_FOUND));
	}
}
