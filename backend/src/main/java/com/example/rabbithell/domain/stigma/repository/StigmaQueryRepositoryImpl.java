package com.example.rabbithell.domain.stigma.repository;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.rabbithell.domain.stigma.dto.request.StigmaCond;
import com.example.rabbithell.domain.stigma.entity.QStigma;
import com.example.rabbithell.domain.stigma.entity.Stigma;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
@RequiredArgsConstructor
public class StigmaQueryRepositoryImpl implements StigmaQueryRepository {

   private final JPAQueryFactory queryFactory;
   private final QStigma stigma = QStigma.stigma;

    @Override
    public List<Stigma> findAllByCondition(StigmaCond cond, Pageable pageable) {
        return queryFactory
            .selectFrom(stigma)
            .where(
                eqIsDeleted(cond.isDeleted()),
                containsName(cond.keyword())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    @Override
    public long countByCondition(StigmaCond cond) {
        return Optional.ofNullable(
            queryFactory
                .select(stigma.count())
                .from(stigma)
                .where(
                    eqIsDeleted(cond.isDeleted()),
                    containsName(cond.keyword())
                )
                .fetchOne()
        ).orElse(0L);
    }

    private BooleanExpression eqIsDeleted(Boolean isDeleted) {
        return isDeleted != null ? stigma.isDeleted.eq(isDeleted) : null;
    }

    private BooleanExpression containsName(String keyword) {
        return keyword != null ? stigma.name.contains(keyword) : null;
    }
}
