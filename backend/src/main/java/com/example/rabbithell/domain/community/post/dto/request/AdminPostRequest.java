package com.example.rabbithell.domain.community.post.dto.request;

import com.example.rabbithell.domain.community.post.entity.AdminPostCategory;
import com.example.rabbithell.domain.community.post.entity.PostCategory;

public record AdminPostRequest(
	String title,
	String content,
	AdminPostCategory adminPostCategory
) {
}
