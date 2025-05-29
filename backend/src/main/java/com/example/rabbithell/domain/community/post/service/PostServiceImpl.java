// package com.example.rabbithell.domain.community.post.service;
//
// import java.util.List;
//
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
//
// import com.example.rabbithell.common.dto.request.CursorPageRequest;
// import com.example.rabbithell.common.dto.response.CursorPageResponse;
// import com.example.rabbithell.common.util.CursorPaginationUtil;
// import com.example.rabbithell.domain.community.post.dto.request.PostRequest;
// import com.example.rabbithell.domain.community.post.dto.response.PostResponse;
// import com.example.rabbithell.domain.community.post.entity.Post;
// import com.example.rabbithell.domain.community.post.repository.PostRepository;
// import com.example.rabbithell.domain.user.model.User;
// import com.example.rabbithell.domain.user.repository.UserRepository;
//
// import lombok.RequiredArgsConstructor;
//
// @Service
// @RequiredArgsConstructor
// public class PostServiceImpl implements PostService {
//
//     private final PostRepository postRepository;
//     private final UserRepository userRepository;
//
//     @Override
//     public PostResponse createPost(String userEmail, PostRequest postRequest) {
//
//         User user = userRepository.findByEmail(userEmail)
//             .orElseThrow(() -> new IllegalArgumentException("유저 없음, 커스텀 예외 설계후 교체"));
//
//         Post post = Post.builder()
//             .title(postRequest.title())
//             .content(postRequest.content())
//             .user(user)
//             .commentCount(0)
//             .isDeleted(false)
//             .build();
//
//         Post savedPost = postRepository.save(post);
//         return PostResponse.fromEntity(savedPost);
//     }
//
//     @Transactional(readOnly = true)
//     @Override
//     public PostResponse getPostById(Long postId) {
//         Post post = postRepository.findByIdAndIsDeletedFalse(postId)
//             .orElseThrow(() -> new IllegalArgumentException("게시글 없음, 커스텀 예외 후 교체"));
//         return PostResponse.fromEntity(post);
//     }
//
//     @Transactional(readOnly = true)
//     @Override
//     public CursorPageResponse<PostResponse> getPostByCursor(CursorPageRequest cursorPageRequest) {
//         List<Post> posts = postRepository.findAllByUserIdAndIsDeletedFalse(cursorPageRequest.getCursor(), cursorPageRequest.getSize());
//
//         List<PostResponse> dtoList = posts.stream()
//             .map(PostResponse::fromEntity).toList();
//
//         return CursorPaginationUtil.paginate(dtoList, cursorPageRequest.getSize(), PostResponse::postId);
//     }
//
//     @Transactional
//     @Override
//     public void deletePost(Long userId, Long postId) {
//         Post post = postRepository.findByIdAndIsDeletedFalse(postId)
//             .orElseThrow(() -> new IllegalArgumentException("게시글 없음, 커스텀 예외 후 교체"));
//
//         if (!post.getUser().getId().equals(userId)) {
//             throw new IllegalArgumentException("작성자 다름, 커스텀 예외 후 교채");
//         }
//
//         post.markAsDelete();
//     }
//
//     @Override
//     public PostResponse updatePost(Long userId, Long postId, PostRequest postRequest) {
//
//         Post post = postRepository.findByIdAndIsDeletedFalse(postId)
//             .orElseThrow(() -> new IllegalArgumentException("게시글 없음, 커스텀 예외 후 교체"));
//
//         if (!post.getUser().getId().equals(userId)) {
//             throw new IllegalArgumentException("작성자 다름, 커스텀 예외 후 교채");
//         }
//
//         post.update(postRequest.title(), postRequest.content());
//
//         return PostResponse.fromEntity(post);
//     }
// }
