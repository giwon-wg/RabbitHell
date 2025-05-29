package com.example.rabbithell.domain.community.comment.service;

import org.springframework.stereotype.Service;

import com.example.rabbithell.common.dto.request.CursorPageRequest;
import com.example.rabbithell.common.dto.response.CursorPageResponse;
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
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final CommentQueryRepository commentQueryRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public CommentResponse create(Long postId, Long userId, CommentRequest request) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
            .orElseThrow(() -> new IllegalArgumentException("게시글 없음, 커스텀예외 추가 후 교체"));

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
            .orElseThrow(() -> new IllegalArgumentException("유저 없음, 커스텀예외 추가 후 교체"));

        Comment comment = Comment.builder()
            .post(post)
            .user(user)
            .content(request.content())
            .build();

        return CommentResponse.fromEntity(commentRepository.save(comment));
    }

    @Override
    public CommentResponse update(Long commentId, Long userId, CommentRequest request) {
        return null;
    }

    @Override
    public void delete(Long commentId, Long userId) {

    }

    @Override
    public CursorPageResponse<CommentResponse> getCommentsByPostWithCursor(Long postId, CursorPageRequest pageRequest) {
        return null;
    }
}
