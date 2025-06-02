package com.example.rabbithell.domain.community.post.repository;

import static com.example.rabbithell.domain.community.post.exception.code.PostExceptionCode.*;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.rabbithell.domain.community.post.entity.AdminPost;
import com.example.rabbithell.domain.community.post.entity.AdminPostCategory;
import com.example.rabbithell.domain.community.post.exception.PostException;

public interface AdminPostRepository extends JpaRepository<AdminPost, Long> {

	// id 기반 조회
	Optional<AdminPost> findByIdAndIsDeletedFalse(Long id);

	// 카테고리 필터 전체 조회 fetch = LAZY 문제 해결
	@Query(value = """
	SELECT a FROM AdminPost a
	JOIN FETCH a.user
	WHERE a.isDeleted = false AND a.adminPostCategory = :category
	""",
		countQuery = """
	SELECT count(a) FROM AdminPost a
	WHERE a.isDeleted = false AND a.adminPostCategory = :category
	""")
	Page<AdminPost> findByIsDeletedFalseAndAdminPostCategory(
		AdminPostCategory category,
		Pageable pageable
	);
	default AdminPost findByIdOrElseThrow(Long id) {

		return findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new PostException(POST_NOT_FOUND));
	}

	default AdminPost findByIdAndValidateOwner(Long id, Long userId) {
		AdminPost adminPost = findByIdOrElseThrow(id);

		if (!adminPost.getUser().getId().equals(userId)) {
			throw new PostException(USER_MISMATCH);
		}
		return adminPost;
	}
}
