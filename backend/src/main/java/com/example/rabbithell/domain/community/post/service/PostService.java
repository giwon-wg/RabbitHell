package com.example.rabbithell.domain.community.post.service;

import org.springframework.data.domain.Pageable;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.community.post.dto.request.PostRequest;
import com.example.rabbithell.domain.community.post.dto.response.PostResponse;

public interface PostService {

	PostResponse createPost(Long userId, PostRequest postRequest);

	PostResponse getPostById(Long postId);

	PageResponse<PostResponse> getAllPosts(Pageable pageable);

	void deletePost(Long userId, Long postId);

	PostResponse updatePost(Long userId, Long postId, PostRequest postRequest);

}
