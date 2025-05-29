package com.example.rabbithell.domain.community.comment.service;

import java.util.List;

import com.example.rabbithell.common.dto.request.CursorPageRequest;
import com.example.rabbithell.common.dto.response.CursorPageResponse;
import com.example.rabbithell.domain.community.comment.dto.request.CommentRequest;
import com.example.rabbithell.domain.community.comment.dto.response.CommentResponse;

public interface CommentService {

    CommentResponse create(Long postId, Long userId, CommentRequest request);

    CommentResponse update(Long commentId, Long userId, CommentRequest request);

    void delete(Long commentId, Long userId);

    CursorPageResponse<CommentResponse> getCommentsByPostWithCursor(Long postId, CursorPageRequest pageRequest);

}
