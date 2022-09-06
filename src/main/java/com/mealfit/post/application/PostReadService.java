package com.mealfit.post.application;


import com.mealfit.exception.post.PostNotFoundException;
import com.mealfit.exception.user.UserNotFoundException;
import com.mealfit.post.domain.Post;
import com.mealfit.post.domain.PostReadRepository;
import com.mealfit.post.presentation.dto.response.PostResponse;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional //(readOnly = true)
public class PostReadService {

    private final UserRepository userRepository;
    private final PostReadRepository postReadRepository;

    //상세 게시글 조회
    public PostResponse getReadOne(Long postId) {
        Post post = postReadRepository.findById(postId)
              .orElseThrow(() -> new PostNotFoundException("게시글이 없습니다."));

        User user = userRepository.findById(post.getUserId())
              .orElseThrow(() -> new UserNotFoundException("없는 회원정보입니다."));

        return PostResponse.builder()
              .postId(post.getId())
              .content(post.getContent())
              .images(post.getImageUrls())
              .nickname(user.getUserProfile().getNickname())
              .profileImage(user.getUserProfile().getProfileImage())
              .like(post.getLikeIt())
              .view(postReadRepository.updateView(postId))
              .view(post.getView())
              .createdAt(post.getCreatedAt())
              .build();
    }

    //전체 게시물 조회
    public List<PostResponse> getReadAll(Pageable pageable, Long lastId) {

        log.info("pageable -> {} ", pageable);
        log.info("lastId -> {} ", lastId);

        Page<Post> postSlice = postReadRepository
              .findAllByIdLessThan(lastId, pageable);

        log.info("result=> {}", postSlice);
        log.info("result=> {}", postSlice.getContent());

        return postToPostsResponseDtos(postSlice);
    }
    //전체 게시물 조회

    private List<PostResponse> postToPostsResponseDtos(Page<Post> postSlice) {
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
                        .like(post.getLikeIt())
                        .createdAt(post.getCreatedAt())
                        .build();
              }
        ).toList();
    }
}
