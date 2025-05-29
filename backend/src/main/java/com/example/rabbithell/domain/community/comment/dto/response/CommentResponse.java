package com.example.rabbithell.domain.community.comment.dto.response;

import java.time.LocalDateTime;

import com.example.rabbithell.domain.community.comment.entity.Comment;

public record CommentResponse(
    Long id,
    String content,
    String writerNickname,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt
) {
    public static CommentResponse fromEntity(Comment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getContent(),
            comment.getUser().getName(),
            comment.getCreatedAt(),
            comment.getModifiedAt()
        );
    }
}
