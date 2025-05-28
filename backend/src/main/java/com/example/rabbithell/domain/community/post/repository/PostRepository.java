package com.example.rabbithell.domain.community.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rabbithell.domain.community.post.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByIsDeletedFalse(Long cursor, int size);

    Optional<Post> findByIdAndIsDeletedFalse(Long id);

}
