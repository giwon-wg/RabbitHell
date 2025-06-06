package com.example.rabbithell.domain.community.post.repository;

import static com.example.rabbithell.domain.community.post.exception.code.PostExceptionCode.*;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.rabbithell.domain.community.post.entity.Post;
import com.example.rabbithell.domain.community.post.entity.PostCategory;
import com.example.rabbithell.domain.community.post.exception.PostException;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	// offset 페이징
	@Query(value = """
	SELECT p FROM Post p
	JOIN FETCH p.user
	WHERE p.isDeleted = false AND p.postCategory = :category
	""",
		countQuery = """
	SELECT COUNT(p) FROM Post p
	WHERE p.isDeleted = false AND p.postCategory = :category
	""")
	Page<Post> findByIsDeletedFalseAndPostCategory(
		PostCategory category,
		Pageable pageable
	);

	// id 기반 조회
	Optional<Post> findByIdAndIsDeletedFalse(Long id);

	default Post findByIdOrElseThrow(Long id) {

		return findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new PostException(POST_NOT_FOUND));
	}

	default Post findByIdAndValidateOwner(Long id, Long userId) {
		Post post = findByIdOrElseThrow(id);

		if (!post.getUser().getId().equals(userId)) {
			throw new PostException(USER_MISMATCH);
		}
		return post;
	}
}
