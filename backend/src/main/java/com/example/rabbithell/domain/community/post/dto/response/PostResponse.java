package com.example.rabbithell.domain.community.post.dto.response;

import java.time.LocalDateTime;

import com.example.rabbithell.domain.community.post.entity.Post;

public record PostResponse(
	Long postId,
	Long userId,
	String userName,
	String title,
	String content,
	Integer commentCount,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {
	public static PostResponse fromEntity(Post post) {
		return new PostResponse(
			post.getId(),
			post.getUser().getId(),
			post.getUser().getName(),
			post.getTitle(),
			post.getContent(),
			post.getCommentCount(),
			post.getCreatedAt(),
			post.getModifiedAt()
		);
	}
}
