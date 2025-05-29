package com.example.rabbithell.domain.community.comment.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.rabbithell.domain.community.comment.entity.Comment;
import com.example.rabbithell.domain.community.comment.entity.QComment;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepositoryImpl implements CommentQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findByPostIdAndCursor(Long postId, Long cursorId, int size) {
        QComment comment = QComment.comment;

        BooleanExpression condition = comment.post.id.eq(postId)
            .and(comment.isDeleted.isFalse());

        if (cursorId != null) {
            condition = condition.and(comment.id.lt(cursorId));
        }

        return jpaQueryFactory
            .selectFrom(comment)
            .where(condition)
            .orderBy(comment.id.desc())
            .limit(size)
            .fetch();
    }
}
