package com.example.rabbithell.domain.community.post.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.community.post.dto.request.AdminPostRequest;
import com.example.rabbithell.domain.community.post.dto.request.PostRequest;
import com.example.rabbithell.domain.community.post.dto.response.AdminPostResponse;
import com.example.rabbithell.domain.community.post.service.AdminPostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admins/posts")
public class AdminPostController {

	private final AdminPostService adminPostService;

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping
	public ResponseEntity<CommonResponse<AdminPostResponse>> adminCreatePost(
		@AuthenticationPrincipal AuthUser authUser,
		@Valid @RequestBody AdminPostRequest adminPostRequest
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"게시글 작성 성공(어드민)",
			adminPostService.adminCreatePost(authUser.getUserId(), adminPostRequest)));
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping("/{postId}")
	public ResponseEntity<CommonResponse<AdminPostResponse>> updatePost(
		@AuthenticationPrincipal AuthUser authUser, // 어드민 권한 이 필요한 부분은 MiniAuthUser
		@PathVariable Long postId,
		@Valid @RequestBody PostRequest request
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"게시글 수정 성공(어드민)",
			adminPostService.updatePost(authUser.getUserId(), postId, request)));
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/{postId}")
	public ResponseEntity<CommonResponse<AdminPostResponse>> deletePost(
		@AuthenticationPrincipal AuthUser authUser,
		@PathVariable Long postId
	) {
		adminPostService.deletePost(authUser.getUserId(), postId);
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"게시글 삭제 성공(어드민)"));
	}

}
