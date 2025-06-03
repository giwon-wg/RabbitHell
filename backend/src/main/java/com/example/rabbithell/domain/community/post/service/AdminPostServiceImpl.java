package com.example.rabbithell.domain.community.post.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.community.post.dto.request.AdminPostRequest;
import com.example.rabbithell.domain.community.post.dto.request.PostRequest;
import com.example.rabbithell.domain.community.post.dto.response.AdminPostResponse;
import com.example.rabbithell.domain.community.post.entity.AdminPost;
import com.example.rabbithell.domain.community.post.entity.AdminPostCategory;
import com.example.rabbithell.domain.community.post.repository.AdminPostRepository;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminPostServiceImpl implements AdminPostService{

	private final AdminPostRepository adminPostRepository;
	private final UserRepository userRepository;

	@Transactional
	@Override
	public AdminPostResponse adminCreatePost(Long userid, AdminPostRequest adminPostRequest) {

		User user = userRepository.findByIdOrElseThrow(userid);

		AdminPost adminPost = new AdminPost(
			user,
			adminPostRequest.title(),
			adminPostRequest.content(),
			adminPostRequest.adminPostCategory()
		);

		AdminPost savedPost = adminPostRepository.save(adminPost);
		return AdminPostResponse.fromEntity(savedPost);

	}

	@Transactional(readOnly = true)
	@Override
	public AdminPostResponse getPostById(Long postId) {
		return AdminPostResponse.fromEntity(adminPostRepository.findByIdOrElseThrow(postId));
	}

	@Transactional
	@Override
	public void deletePost(Long userId, Long postId) {
		AdminPost adminPost = adminPostRepository.findByIdAndValidateOwner(postId, userId);

		adminPost.markAsDeleted();
	}

	@Transactional
	@Override
	public AdminPostResponse updatePost(Long userId, Long postId, PostRequest postRequest) {
		AdminPost adminPost = adminPostRepository.findByIdAndValidateOwner(postId, userId);

		adminPost.update(postRequest.title(), postRequest.content());

		return AdminPostResponse.fromEntity(adminPost);
	}

	@Transactional(readOnly = true)
	@Override
	public PageResponse<AdminPostResponse> getPostsByCategory(AdminPostCategory category, Pageable pageable) {
		Page<AdminPost> posts = adminPostRepository.findByIsDeletedFalseAndAdminPostCategory(category, pageable);

		List<AdminPostResponse> content = posts.stream()
			.map(AdminPostResponse::fromEntity)
			.toList();

		return PageResponse.of(content, posts);
	}
}
