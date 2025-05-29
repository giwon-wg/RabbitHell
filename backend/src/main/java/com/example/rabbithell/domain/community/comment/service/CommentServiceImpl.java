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
import com.example.rabbithell.domain.community.comment.exception.CommentException;
import com.example.rabbithell.domain.community.comment.exception.code.CommentExceptionCode;
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
    @Transactional
    public CommentResponse create(Long postId, Long userId, CommentRequest request) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
            .orElseThrow(() -> new CommentException(CommentExceptionCode.POST_NOT_FOUND));

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
            .orElseThrow(() -> new CommentException(CommentExceptionCode.USER_NOT_FOUND));

        Comment comment = Comment.builder()
            .post(post)
            .user(user)
            .content(request.content())
            .isDeleted(false)
            .build();

        post.increaseCommentCount();
        return CommentResponse.fromEntity(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentResponse update(Long commentId, Long userId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentException(CommentExceptionCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(userId)) {
            throw new CommentException(CommentExceptionCode.USER_MISMATCH);
        }

        comment.update(request.content());
        return CommentResponse.fromEntity(comment);
    }

    @Override
    @Transactional
    public void delete(Long postId, Long commentId, Long userId) {

        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
            .orElseThrow(() -> new CommentException(CommentExceptionCode.POST_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentException(CommentExceptionCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(userId)) {
            throw new CommentException(CommentExceptionCode.USER_MISMATCH);
        }

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
