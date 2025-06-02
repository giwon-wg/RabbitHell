package com.example.rabbithell.domain.community.post.service;

import org.springframework.data.domain.Pageable;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.community.post.dto.request.PostRequest;
import com.example.rabbithell.domain.community.post.dto.response.PostResponse;
import com.example.rabbithell.domain.community.post.entity.PostCategory;

public interface PostService {

	PostResponse createPost(Long userId, PostRequest postRequest);

	PostResponse getPostById(Long postId);

	void deletePost(Long userId, Long postId);

	PostResponse updatePost(Long userId, Long postId, PostRequest postRequest);

	PageResponse<PostResponse> getPostsByCategory(PostCategory postCategory, Pageable pageable);

}
