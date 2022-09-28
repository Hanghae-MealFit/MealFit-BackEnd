package com.mealfit.post.application;


import static java.util.stream.Collectors.toList;

import com.mealfit.exception.post.PostNotFoundException;
import com.mealfit.post.application.dto.request.PostDetailRequestDto;
import com.mealfit.post.application.dto.request.PostListRequestDto;
import com.mealfit.post.domain.Post;
import com.mealfit.post.domain.PostLikeRepository;
import com.mealfit.post.domain.PostReadRepository;
import com.mealfit.post.presentation.dto.response.PostResponse;
import com.mealfit.user.domain.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        return PostResponse.builder()
              .postId(post.getId())
              .content(post.getContent())
              .images(post.getImageUrls())
              .nickname(post.getUser().getUserProfile().getNickname())
              .profileImage(post.getUser().getUserProfile().getProfileImage())
              .liked(dto.getUserId() != null && postLikeRepository.existsByPostIdAndUserId(
                    post.getId(),
                    dto.getUserId()))
              .like(post.getLikeIt())
              .createdAt(post.getCreatedAt())
              .build();
    }

    //전체 게시물 조회
    public List<PostResponse> getReadAll(PostListRequestDto dto) {

        return postReadRepository.findAllByIdLessThan(dto.getLastId(), dto.getPageable())
              .stream().map(post -> {
                  return PostResponse.builder()
                        .postId(post.getId())
                        .content(post.getContent())
                        .images(post.getImageUrls())
                        .nickname(post.getUser().getUserProfile().getNickname())
                        .profileImage(post.getUser().getUserProfile().getProfileImage())
                        .view(post.getView())
                        .liked(dto.getUserId() != null && postLikeRepository.existsByPostIdAndUserId(post.getId(), dto.getUserId()))
                        .like(post.getLikeIt())
                        .createdAt(post.getCreatedAt())
                        .build();
                    }
              ).collect(toList());
    }
}
