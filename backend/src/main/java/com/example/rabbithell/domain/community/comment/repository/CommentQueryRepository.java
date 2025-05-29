package com.example.rabbithell.domain.community.comment.repository;

import java.util.List;

import com.example.rabbithell.domain.community.comment.entity.Comment;

public interface CommentQueryRepository {

    List<Comment> findByPostIdAndCursor(Long postId, Long cursorId, int size);

}
