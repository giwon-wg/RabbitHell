package com.example.rabbithell.domain.community.comment.service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.common.dto.request.CursorPageRequest;
import com.example.rabbithell.common.dto.response.CursorPageResponse;
import com.example.rabbithell.common.util.CursorPaginationUtil;
import com.example.rabbithell.domain.community.comment.dto.request.CommentRequest;
import com.example.rabbithell.domain.community.comment.dto.response.CommentResponse;
import com.example.rabbithell.domain.community.comment.entity.Comment;
import com.example.rabbithell.domain.community.comment.repository.CommentQueryRepository;
import com.example.rabbithell.domain.community.comment.repository.CommentRepository;
import com.example.rabbithell.domain.community.post.entity.Post;
import com.example.rabbithell.domain.community.post.repository.PostRepository;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;
	private final CommentQueryRepository commentQueryRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public CommentResponse create(Long postId, Long userId, CommentRequest request) {

		Post post = postRepository.finByIdOrElseThrow(postId);

		User user = userRepository.findByIdOrElseThrow(userId);

		Comment comment = new Comment(
			user,
			post,
			request.content()
		);

		post.increaseCommentCount();
		return CommentResponse.fromEntity(commentRepository.save(comment));
	}

	@Override
	@Transactional
	public CommentResponse update(Long commentId, Long userId, CommentRequest request) {

		Comment comment = commentRepository.findByIdAndValidateOwner(commentId, userId);

		comment.update(request.content());

		return CommentResponse.fromEntity(comment);
	}

	@Override
	@Transactional
	public void delete(Long postId, Long commentId, Long userId) {

		Post post = postRepository.finByIdOrElseThrow(postId);

		Comment comment = commentRepository.findByIdAndValidateOwner(commentId, userId);

		post.decreaseCommentCount();

		comment.markAsDeleted();
	}

	@Override
	@Transactional(readOnly = true)
	public CursorPageResponse<CommentResponse> getCommentsByPostWithCursor(Long postId, CursorPageRequest pageRequest) {
		List<Comment> comments = commentQueryRepository.findByPostIdAndCursor(
			postId,
			pageRequest.cursorId(),
			pageRequest.sizeOrDefault()
		);

		List<CommentResponse> responses = comments.stream()
			.map(CommentResponse::fromEntity)
			.toList();

		return CursorPaginationUtil.paginate(responses, pageRequest.sizeOrDefault(), CommentResponse::id);
	}
}
