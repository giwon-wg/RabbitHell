package com.example.rabbithell.domain.community.post.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rabbithell.domain.community.post.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	// offset 페이징
	Page<Post> findAllByIsDeletedFalse(Pageable pageable);

	// id 기반 조회
	Optional<Post> findByIdAndIsDeletedFalse(Long id);

}
