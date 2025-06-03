package com.example.rabbithell.domain.community.post.dto.request;

import com.example.rabbithell.domain.community.post.entity.PostCategory;

public record PostRequest(
	String title,
	String content,
	PostCategory postCategory
) {
}
