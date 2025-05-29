package com.example.rabbithell.domain.community.post.service;

import com.example.rabbithell.common.dto.request.CursorPageRequest;
import com.example.rabbithell.common.dto.response.CursorPageResponse;
import com.example.rabbithell.domain.community.post.dto.request.PostRequest;
import com.example.rabbithell.domain.community.post.dto.response.PostResponse;

public interface PostService {

    PostResponse createPost(String userEmail, PostRequest postRequest);

    PostResponse getPostById(Long postId);

    CursorPageResponse<PostResponse> getPostByCursor(CursorPageRequest cursorPageRequest);

    void deletePost(Long userId, Long postId);

    PostResponse updatePost(Long userId, Long postId, PostRequest postRequest);

}
