package com.example.rabbithell.domain.community.comment.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.dto.request.CursorPageRequest;
import com.example.rabbithell.common.dto.response.CursorPageResponse;
import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.community.comment.dto.request.CommentRequest;
import com.example.rabbithell.domain.community.comment.dto.response.CommentResponse;
import com.example.rabbithell.domain.community.comment.service.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<CommonResponse<CommentResponse>> createComment(
		@PathVariable Long postId,
		@AuthenticationPrincipal AuthUser authUser,
		@Valid @RequestBody CommentRequest request
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"댓글 생성 성공",
			commentService.create(postId, authUser.getUserId(), request)));
	}

	@GetMapping
	public ResponseEntity<CommonResponse<CursorPageResponse<CommentResponse>>> getComments(
		@PathVariable Long postId,
		@Valid CursorPageRequest pageRequest
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"댓글 조회 성공",
			commentService.getCommentsByPostWithCursor(postId, pageRequest)));
	}

	@PutMapping("/{commentId}")
	public ResponseEntity<CommonResponse<CommentResponse>> updateComment(
		@PathVariable Long postId, // 일단 적어둠
		@PathVariable Long commentId,
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody @Valid CommentRequest request
	) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"댓글 수정 성공",
			commentService.update(commentId, authUser.getUserId(), request)));
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<CommonResponse<Void>> deleteComment(
		@PathVariable Long postId,
		@PathVariable Long commentId,
		@AuthenticationPrincipal AuthUser authUser
	) {
		commentService.delete(postId, commentId, authUser.getUserId());
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"댓글 삭제 성공"));
	}

}
