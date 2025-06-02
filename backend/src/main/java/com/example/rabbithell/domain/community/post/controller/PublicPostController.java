package com.example.rabbithell.domain.community.post.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.community.post.dto.response.AdminPostResponse;
import com.example.rabbithell.domain.community.post.entity.AdminPostCategory;
import com.example.rabbithell.domain.community.post.service.AdminPostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pubic/posts")
public class PublicPostController {

	private final AdminPostService adminPostService;

	@GetMapping("/{postId}")
	public ResponseEntity<CommonResponse<AdminPostResponse>> adminGetPost(@PathVariable Long postId) {
		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"게시글 단건 조회 성공(어드민)",
			adminPostService.getPostById(postId)));
	}

	@GetMapping
	public ResponseEntity<CommonResponse<PageResponse<AdminPostResponse>>> getAllPosts(
		@RequestParam AdminPostCategory category,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		PageResponse<AdminPostResponse> response = adminPostService.getPostsByCategory(category, pageable);

		return ResponseEntity.ok(CommonResponse.of(
			true,
			HttpStatus.OK.value(),
			"게시글 전체 조회 성공(어드민)",
			response));
	}
}
