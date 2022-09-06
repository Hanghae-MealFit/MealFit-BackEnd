package com.mealfit.post.application;

import com.mealfit.common.storageService.StorageService;
import com.mealfit.exception.post.NoPostContentException;
import com.mealfit.exception.post.NoPostImageException;
import com.mealfit.exception.post.NotPostWriterException;
import com.mealfit.exception.post.PostNotFoundException;
import com.mealfit.post.application.dto.request.PostCreateRequestDto;
import com.mealfit.post.application.dto.request.PostDeleteReqeustDto;
import com.mealfit.post.application.dto.request.PostUpdateRequestDto;
import com.mealfit.post.domain.Post;
import com.mealfit.post.domain.PostImage;
import com.mealfit.post.domain.PostRepository;
import com.mealfit.post.presentation.dto.response.PostCUDResponse;
import java.util.List;
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

    private final PostRepository postRepository;
    private final StorageService storageService;

    public PostCUDResponse createPost(PostCreateRequestDto requestDto) {
        validateContent(requestDto.getContent());

        Post postEntity = requestDto.toEntity();
        List<MultipartFile> uploadImages = requestDto.getPostImageList();

        validateImages(uploadImages);

        List<PostImage> postImages = saveImages(uploadImages)
              .stream()
              .map(PostImage::new)
              .collect(Collectors.toList());

        postEntity.addPostImages(postImages);

        Post savedPost = postRepository.save(postEntity);

        return new PostCUDResponse(savedPost);
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

        validateUser(requestDto.getUserId(), post.getUserId());

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
            throw new NotPostWriterException("작성자가 아니므로, 해당 게시글을 수정할 수 없습니다.");
        }
    }

    //게시글 삭제
    public Long deletePost(PostDeleteReqeustDto dto) {
        //유효성 검사
        Post post = findByPostId(dto.getPostId());

        //작성자 검사
        validateUser(dto.getUserId(), post.getUserId());

        postRepository.deleteById(dto.getPostId());

        return dto.getPostId();
    }

    private Post findByPostId(Long postId) {
        return postRepository.findById(postId)
              .orElseThrow(() -> new PostNotFoundException("해당 게시글이 없습니다."));
    }
}

