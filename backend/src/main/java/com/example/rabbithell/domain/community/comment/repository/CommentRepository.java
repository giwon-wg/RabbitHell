package com.example.rabbithell.domain.community.comment.repository;

import static com.example.rabbithell.domain.community.comment.exception.code.CommentExceptionCode.*;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.community.comment.entity.Comment;
import com.example.rabbithell.domain.community.comment.exception.CommentException;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	Optional<Comment> findByIdAndIsDeletedFalse(Long id);

	default Comment finByIdOrElseThrow(Long id) {
		return findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));
	}

	default Comment findByIdAndValidateOwner(Long id, Long userId) {
		Comment comment = finByIdOrElseThrow(id);
		if (!comment.getUser().getId().equals(userId)) {
			throw new CommentException(USER_MISMATCH);
		}
		return comment;
	}

}
