package com.mealfit.post.application;


import com.mealfit.exception.post.PostNotFoundException;
import com.mealfit.exception.user.UserNotFoundException;
import com.mealfit.post.application.dto.request.PostDetailRequestDto;
import com.mealfit.post.application.dto.request.PostListRequestDto;
import com.mealfit.post.domain.Post;
import com.mealfit.post.domain.PostLikeRepository;
import com.mealfit.post.domain.PostReadRepository;
import com.mealfit.post.presentation.dto.response.PostResponse;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostReadService {

    private final UserRepository userRepository;
    private final PostReadRepository postReadRepository;
    private final PostLikeRepository postLikeRepository;

    //상세 게시글 조회
    @Transactional
    public PostResponse getReadOne(PostDetailRequestDto dto) {
        Post post = postReadRepository.findById(dto.getPostId())
              .orElseThrow(() -> new PostNotFoundException("게시글이 없습니다."));

        post.plusView();

        User user = userRepository.findById(post.getUserId())
              .orElseThrow(() -> new UserNotFoundException("없는 회원정보입니다."));

        return PostResponse.builder()
              .postId(post.getId())
              .content(post.getContent())
              .images(post.getImageUrls())
              .nickname(user.getUserProfile().getNickname())
              .profileImage(user.getUserProfile().getProfileImage())
              .liked(dto.getUserId() != null && postLikeRepository.existsByPostIdAndUserId(post.getId(),
                    dto.getUserId()))
              .like(post.getLikeIt())
              .createdAt(post.getCreatedAt())
              .build();
    }

    //전체 게시물 조회
    public List<PostResponse> getReadAll(PostListRequestDto dto) {

        Page<Post> postSlice = postReadRepository
              .findAllByIdLessThan(dto.getLastId(), dto.getPageable());

        return postToPostsResponseDtos(postSlice, dto.getUserId());
    }
    //전체 게시물 조회

    private List<PostResponse> postToPostsResponseDtos(Page<Post> postSlice, Long userId) {
        return postSlice.map(post -> {
                  User user = userRepository.findById(post.getUserId())
                        .orElseThrow(() -> new UserNotFoundException("없는 회원입니다."));

                  return PostResponse.builder()
                        .postId(post.getId())
                        .content(post.getContent())
                        .images(post.getImageUrls())
                        .nickname(user.getUserProfile().getNickname())
                        .profileImage(user.getUserProfile().getProfileImage())
                        .view(post.getView())
                        .liked(userId != null && postLikeRepository.existsByPostIdAndUserId(post.getId(),
                              userId))
                        .like(post.getLikeIt())
                        .createdAt(post.getCreatedAt())
                        .build();
              }
        ).toList();
    }
}
