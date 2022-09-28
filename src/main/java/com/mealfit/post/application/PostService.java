package com.mealfit.post.application;

import com.mealfit.common.storageService.StorageService;
import com.mealfit.exception.authentication.UnAuthorizedUserException;
import com.mealfit.exception.post.NoPostContentException;
import com.mealfit.exception.post.NoPostImageException;
import com.mealfit.exception.post.PostNotFoundException;
import com.mealfit.exception.user.UserNotFoundException;
import com.mealfit.post.application.dto.request.PostCreateRequestDto;
import com.mealfit.post.application.dto.request.PostDeleteReqeustDto;
import com.mealfit.post.application.dto.request.PostLikeRequestDto;
import com.mealfit.post.application.dto.request.PostUpdateRequestDto;
import com.mealfit.post.domain.Post;
import com.mealfit.post.domain.PostImage;
import com.mealfit.post.domain.PostLike;
import com.mealfit.post.domain.PostLikeRepository;
import com.mealfit.post.domain.PostRepository;
import com.mealfit.post.presentation.dto.response.PostCUDResponse;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final StorageService storageService;

    public PostCUDResponse write(PostCreateRequestDto requestDto) {
        validateContent(requestDto.getContent());

        Post postEntity = createPost(requestDto);

        Post savedPost = postRepository.save(postEntity);

        return new PostCUDResponse(savedPost);
    }

    private Post createPost(PostCreateRequestDto requestDto) {
        Post postEntity = requestDto.toEntity();

        User user = userRepository.findById(requestDto.getUserId())
              .orElseThrow(() -> new UserNotFoundException("없는 회원입니다."));

        postEntity.settingUserInfo(user);

        List<MultipartFile> uploadImages = requestDto.getPostImageList();

        validateImages(uploadImages);

        List<PostImage> postImages = saveImages(uploadImages)
              .stream()
              .map(PostImage::new)
              .collect(Collectors.toList());

        postEntity.addPostImages(postImages);

        return postEntity;
    }

    private static void validateImages(List<MultipartFile> uploadImages) {
        if (uploadImages == null) {
            throw new NoPostImageException("uploadImages를 가져오지 못했습니다.");
        }
    }

    private List<String> saveImages(List<MultipartFile> uploadImages) {
        return storageService.uploadMultipartFile(uploadImages, "post/");
    }

    private void validateContent(String content) {
        if (content == null) {
            throw new NoPostContentException("내용을 넣어주세요.");
        }
    }

    // 게시글 수정
    public PostCUDResponse updatePost(PostUpdateRequestDto requestDto) {

        validateContent(requestDto.getContent());
        validateImages(requestDto.getPostImageList());

        Post post = findByPostId(requestDto.getPostId());

        validateUser(requestDto.getUserId(), post.getUser().getId());

        // 사진 갈아끼우기
        List<MultipartFile> uploadImages = requestDto.getPostImageList();

        List<String> saveImageUrls = saveImages(uploadImages);

        List<PostImage> postImages = saveImageUrls.stream()
              .map(PostImage::new)
              .collect(Collectors.toList());

        post.replacePostImages(postImages);

        post.updateContent(requestDto.getContent());

        return new PostCUDResponse(post);
    }

    private void validateUser(Long userId, Long writerId) {
        if (!userId.equals(writerId)) {
            throw new UnAuthorizedUserException("작성자가 아니므로, 해당 게시글을 수정할 수 없습니다.");
        }
    }

    //게시글 삭제
    public Long deletePost(PostDeleteReqeustDto dto) {
        //유효성 검사
        Post post = findByPostId(dto.getPostId());

        //작성자 검사
        validateUser(dto.getUserId(), post.getUser().getId());

        postRepository.deleteById(dto.getPostId());

        return dto.getPostId();
    }

    private Post findByPostId(Long postId) {
        return postRepository.findById(postId)
              .orElseThrow(() -> new PostNotFoundException("해당 게시글이 없습니다."));
    }

    /**
     * 좋아요 좋아요성공 true 좋아요취소 false
     */
    public boolean saveLike(PostLikeRequestDto dto) {
        Optional<PostLike> findLike = postLikeRepository.findByPostIdAndUserId(dto.getPostId(),
              dto.getUserId());

        Post post = postRepository.findById(dto.getPostId())
              .orElseThrow(() -> new PostNotFoundException("해당 게시글이 없습니다."));

        if (findLike.isEmpty()) {
            PostLike postLike = PostLike.builder()
                  .postId(dto.getPostId())
                  .userId(dto.getUserId())
                  .build();
            postLikeRepository.save(postLike);
            post.like();

            return true;
        } else {
            postLikeRepository.deleteByPostIdAndUserId(dto.getPostId(), dto.getUserId());
            post.unLike();
            return false;
        }
    }
}

