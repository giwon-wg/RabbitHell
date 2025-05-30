package com.example.rabbithell.domain.community.post.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.community.post.dto.request.PostRequest;
import com.example.rabbithell.domain.community.post.dto.response.PostResponse;
import com.example.rabbithell.domain.community.post.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;

	@PostMapping
	public ResponseEntity<CommonResponse<PostResponse>> createPost(
		@AuthenticationPrincipal AuthUser authUser,
		@Valid @RequestBody PostRequest postRequest
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"게시글 생성 성공",
			postService.createPost(authUser.getUserId(), postRequest)));
	}

	@GetMapping("/{postId}")
	public ResponseEntity<CommonResponse<PostResponse>> getPost(@PathVariable Long postId) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"게시글 단건 조회 성공",
			postService.getPostById(postId)));
	}

	@GetMapping
	public ResponseEntity<CommonResponse<PageResponse<PostResponse>>> getAllPosts(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		PageResponse<PostResponse> response = postService.getAllPosts(pageable);

		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"게시글 전체 조회 성공",
			response));
	}

	@PutMapping("/{postId}")
	public ResponseEntity<CommonResponse<PostResponse>> updatPost(
		@AuthenticationPrincipal AuthUser authUser,
		@PathVariable Long postId,
		@Valid @RequestBody PostRequest request
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"게시글 수정 성공",
			postService.updatePost(authUser.getUserId(), postId, request)));
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<CommonResponse<PostController>> deletePost(
		@AuthenticationPrincipal AuthUser authUser,
		@PathVariable Long postId
	) {
		postService.deletePost(authUser.getUserId(), postId);
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"게시글 삭제 성공"));
	}

}
