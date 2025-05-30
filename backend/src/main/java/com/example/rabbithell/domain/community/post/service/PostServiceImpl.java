package com.example.rabbithell.domain.community.post.service;

import static com.example.rabbithell.domain.community.post.exception.code.PostExceptionCode.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.community.post.dto.request.PostRequest;
import com.example.rabbithell.domain.community.post.dto.response.PostResponse;
import com.example.rabbithell.domain.community.post.entity.Post;
import com.example.rabbithell.domain.community.post.exception.PostException;
import com.example.rabbithell.domain.community.post.repository.PostRepository;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;

	@Override
	public PostResponse createPost(Long userId, PostRequest postRequest) {

		User user = userRepository.findByIdAndIsDeletedFalse(userId)
			.orElseThrow(() -> new PostException(USER_NOT_FOUND));

		Post post = Post.builder()
			.title(postRequest.title())
			.content(postRequest.content())
			.user(user)
			.commentCount(0)
			.isDeleted(false)
			.build();

		Post savedPost = postRepository.save(post);
		return PostResponse.fromEntity(savedPost);
	}

	@Transactional(readOnly = true)
	@Override
	public PostResponse getPostById(Long postId) {
		Post post = postRepository.findByIdAndIsDeletedFalse(postId)
			.orElseThrow(() -> new PostException(POST_NOT_FOUND));
		return PostResponse.fromEntity(post);
	}

	@Transactional(readOnly = true)
	@Override
	public PageResponse<PostResponse> getAllPosts(Pageable pageable) {
		Page<Post> page = postRepository.findAllByIsDeletedFalse(pageable);

		List<PostResponse> dtoList = page.getContent().stream()
			.map(PostResponse::fromEntity)
			.toList();

		return PageResponse.of(dtoList, page);
	}

	@Transactional
	@Override
	public void deletePost(Long userId, Long postId) {
		Post post = postRepository.findByIdAndIsDeletedFalse(postId)
			.orElseThrow(() -> new PostException(POST_NOT_FOUND));

		if (!post.getUser().getId().equals(userId)) {
			throw new PostException(USER_MISMATCH);
		}

		post.markAsDeleted();
	}

	@Transactional
	@Override
	public PostResponse updatePost(Long userId, Long postId, PostRequest postRequest) {

		Post post = postRepository.findByIdAndIsDeletedFalse(postId)
			.orElseThrow(() -> new PostException(POST_NOT_FOUND));

		if (!post.getUser().getId().equals(userId)) {
			throw new PostException(USER_MISMATCH);
		}

		post.update(postRequest.title(), postRequest.content());

		return PostResponse.fromEntity(post);
	}
}
