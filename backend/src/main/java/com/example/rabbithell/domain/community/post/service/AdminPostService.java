package com.example.rabbithell.domain.community.post.service;

import org.springframework.data.domain.Pageable;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.community.post.dto.request.AdminPostRequest;
import com.example.rabbithell.domain.community.post.dto.request.PostRequest;
import com.example.rabbithell.domain.community.post.dto.response.AdminPostResponse;
import com.example.rabbithell.domain.community.post.entity.AdminPostCategory;

public interface AdminPostService {

	AdminPostResponse adminCreatePost(Long id, AdminPostRequest adminPostRequest);

	AdminPostResponse getPostById(Long postId);

	void deletePost(Long userId, Long postId);

	AdminPostResponse updatePost(Long userId, Long postId, PostRequest postRequest);

	PageResponse<AdminPostResponse> getPostsByCategory(AdminPostCategory category, Pageable pageable);
}
