package com.example.rabbithell.domain.community.post.dto.response;

import java.time.LocalDateTime;

import com.example.rabbithell.domain.community.post.entity.AdminPost;

public record AdminPostResponse(
	Long postId,
	Long userId,
	String userName,
	String title,
	String content,
	Integer commentCount,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {
	public static AdminPostResponse fromEntity(AdminPost adminPost) {
		return new AdminPostResponse(
			adminPost.getId(),
			adminPost.getUser().getId(),
			adminPost.getUser().getName(),
			adminPost.getTitle(),
			adminPost.getContent(),
			adminPost.getCommentCount(),
			adminPost.getCreatedAt(),
			adminPost.getModifiedAt()
		);
	}
}
