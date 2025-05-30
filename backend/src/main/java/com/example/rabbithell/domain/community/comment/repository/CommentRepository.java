package com.example.rabbithell.domain.community.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.community.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByPostIdAndIsDeletedFalseOrderByIdDesc(Long postId);

}
