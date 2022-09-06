package com.mealfit.unit.post.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mealfit.common.factory.PostFactory;
import com.mealfit.common.storageService.StorageService;
import com.mealfit.exception.post.NoPostContentException;
import com.mealfit.exception.post.NoPostImageException;
import com.mealfit.exception.post.NotPostWriterException;
import com.mealfit.exception.post.PostNotFoundException;
import com.mealfit.post.application.PostService;
import com.mealfit.post.application.dto.request.PostCreateRequestDto;
import com.mealfit.post.application.dto.request.PostDeleteReqeustDto;
import com.mealfit.post.application.dto.request.PostUpdateRequestDto;
import com.mealfit.post.domain.Post;
import com.mealfit.post.domain.PostImage;
import com.mealfit.post.domain.PostRepository;
import com.mealfit.post.presentation.dto.response.PostCUDResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@DisplayName("PostServiceTest - 게시글 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private StorageService storageService;

    @DisplayName("createPost() 메서드는")
    @Nested
    class Testing_createPost {

        @DisplayName("모든 입력이 정상적이면 성공적으로 저장한다.")
        @Test
        void success() {

            // given
            Post post = PostFactory.imagePost(1L, 1L, "content",
                  List.of(
                        new PostImage("https://github.com/testImage1.jpeg"),
                        new PostImage("https://github.com/testImage2.jpeg")
                  ));

            PostCreateRequestDto requestDto = PostCreateRequestDto.builder()
                  .userId(1L)
                  .content("content")
                  .postImageList(List.of(
                        new MockMultipartFile("https://github.com/testImage1.jpeg",
                              "<<image>>".getBytes()),
                        new MockMultipartFile("https://github.com/testImage1.jpeg",
                              "<<image>>".getBytes())
                  ))
                  .build();

            given(storageService.uploadMultipartFile(anyList(), anyString()))
                  .willReturn(List.of(
                        "https://github.com/testImage1.jpeg",
                        "https://github.com/testImage2.jpeg"));

            given(postRepository.save(any(Post.class))).willReturn(post);

            // when
            PostCUDResponse responseDto = postService.createPost(requestDto);

            assertThat(responseDto.getUserId()).isEqualTo(post.getUserId());
            assertThat(responseDto.getContent()).isEqualTo(post.getContent());
            assertThat(responseDto.getImageUrls()).usingRecursiveComparison()
                  .isEqualTo(post.getImageUrls());
            assertThat(responseDto.getImageUrls()).hasSize(2);

            verify(storageService, times(1))
                  .uploadMultipartFile(anyList(), anyString());
            verify(postRepository, times(1)).save(any(Post.class));
        }

        @DisplayName("content가 비어있다면 실패한다.")
        @Test
        void content_empty_fail() {
            PostCreateRequestDto requestDto = PostCreateRequestDto.builder()
                  .userId(1L)
                  .postImageList(List.of(
                        new MockMultipartFile("https://github.com/testImage1.jpeg",
                              "<<image>>".getBytes()),
                        new MockMultipartFile("https://github.com/testImage1.jpeg",
                              "<<image>>".getBytes())
                  ))
                  .build();

            assertThatThrownBy(() -> postService.createPost(requestDto))
                  .isInstanceOf(NoPostContentException.class);
        }

        @DisplayName("이미지가 비어있다면 실패한다.")
        @Test
        void postImageList_empty_fail() {
            // given
            Post post = PostFactory.imagePost(1L, 1L, "content", new ArrayList<>());

            PostCreateRequestDto requestDto = PostCreateRequestDto.builder()
                  .userId(1L)
                  .content("content")
                  .build();

            // when
            assertThatThrownBy(() -> postService.createPost(requestDto))
                  .isInstanceOf(NoPostImageException.class);
        }
    }

    @DisplayName("updatePost() 메서드는")
    @Nested
    class Testing_updatePost {

        @DisplayName("모든 입력이 정상적이면 성공적으로 저장한다.")
        @Test
        void success() {

            // given
            Post post = PostFactory.imagePost(1L, 1L, "content",
                  new ArrayList<>());

            PostUpdateRequestDto requestDto = PostUpdateRequestDto.builder()
                  .postId(1L)
                  .userId(1L)
                  .content("new Content")
                  .postImageList(List.of(
                        new MockMultipartFile("https://github.com/testImage1.jpeg",
                              "<<image>>".getBytes()),
                        new MockMultipartFile("https://github.com/testImage2.jpeg",
                              "<<image>>".getBytes())
                  ))
                  .build();

            given(postRepository.findById(1L)).willReturn(Optional.of(post));
            given(storageService.uploadMultipartFile(anyList(), anyString()))
                  .willReturn(List.of("https://github.com/testImage1.jpeg",
                        "https://github.com/testImage2.jpeg"));

            // when
            PostCUDResponse responseDto = postService.updatePost(requestDto);
            System.out.println(responseDto);

            assertThat(responseDto.getUserId()).isEqualTo(requestDto.getUserId());
            assertThat(responseDto.getContent()).isEqualTo(requestDto.getContent());
            assertThat(responseDto.getImageUrls()).usingRecursiveComparison()
                  .isEqualTo(post.getImageUrls());
            assertThat(responseDto.getImageUrls()).hasSize(2);

            verify(postRepository, times(1))
                  .findById(1L);
            verify(storageService, times(1))
                  .uploadMultipartFile(anyList(), anyString());
        }

        @DisplayName("content가 비어있다면 실패한다.")
        @Test
        void content_empty_fail() {

            // given
            PostUpdateRequestDto requestDto = PostUpdateRequestDto.builder()
                  .postId(1L)
                  .userId(1L)
                  .postImageList(List.of(
                        new MockMultipartFile("https://github.com/testImage1.jpeg",
                              "<<image>>".getBytes()),
                        new MockMultipartFile("https://github.com/testImage2.jpeg",
                              "<<image>>".getBytes())
                  ))
                  .build();

            // when then
            assertThatThrownBy(() -> postService.updatePost(requestDto))
                  .isInstanceOf(NoPostContentException.class);
        }

        @DisplayName("이미지가 비어있으면 실패한다.")
        @Test
        void postImageList_empty_fail() {
            // given
            PostUpdateRequestDto requestDto = PostUpdateRequestDto.builder()
                  .userId(1L)
                  .content("new Content")
                  .build();

            // when then
            assertThatThrownBy(() -> postService.updatePost(requestDto))
                  .isInstanceOf(NoPostImageException.class);
        }

        @DisplayName("글쓴이가 아니면 실패한다.")
        @Test
        void postImageList_empty_success() {
            // given
            Post post = PostFactory.imagePost(1L, 1L, "content", new ArrayList<>());

            PostUpdateRequestDto requestDto = PostUpdateRequestDto.builder()
                  .postId(1L)
                  .userId(2L)
                  .content("new Content")
                  .postImageList(List.of(
                        new MockMultipartFile("https://github.com/testImage1.jpeg",
                              "<<image>>".getBytes()),
                        new MockMultipartFile("https://github.com/testImage2.jpeg",
                              "<<image>>".getBytes())
                  ))
                  .build();

            given(postRepository.findById(1L)).willReturn(Optional.of(post));


            // when then
            assertThatThrownBy(() -> postService.updatePost(requestDto))
                  .isInstanceOf(NotPostWriterException.class);
        }

        @DisplayName("게시글이 없는 경우 실패한다.")
        @Test
        void no_post_fail() {
            // given
            PostUpdateRequestDto requestDto = PostUpdateRequestDto.builder()
                  .postId(1L)
                  .userId(2L)
                  .content("new Content")
                  .postImageList(List.of(
                        new MockMultipartFile("https://github.com/testImage1.jpeg",
                              "<<image>>".getBytes()),
                        new MockMultipartFile("https://github.com/testImage2.jpeg",
                              "<<image>>".getBytes())
                  ))
                  .build();

            given(postRepository.findById(anyLong())).willReturn(Optional.empty());

            // when then
            assertThatThrownBy(() -> postService.updatePost(requestDto))
                  .isInstanceOf(PostNotFoundException.class);

            // verify
            verify(postRepository, times(1))
                  .findById(1L);
        }
    }

    @DisplayName("deletePost() 메서드는")
    @Nested
    class Testing_deletePost {

        @DisplayName("모든 값이 정상인 경우 성공한다.")
        @Test
        void success() {
            // given
            Post post = PostFactory.imagePost(1L, 1L, "content", new ArrayList<>());

            PostDeleteReqeustDto requestDto = new PostDeleteReqeustDto(1L, 1L);

            given(postRepository.findById(requestDto.getPostId())).willReturn(Optional.of(post));

            // when then
            postService.deletePost(requestDto);

            // verify
            verify(postRepository, times(1))
                  .findById(1L);
            verify(postRepository, times(1))
                  .deleteById(1L);
        }

        @DisplayName("게시글이 없는 경우 실패한다.")
        @Test
        void no_post_fail() {
            // given
            PostDeleteReqeustDto requestDto = new PostDeleteReqeustDto(1L, 1L);

            given(postRepository.findById(anyLong())).willReturn(Optional.empty());

            // when then
            assertThatThrownBy(() -> postService.deletePost(requestDto))
                  .isInstanceOf(PostNotFoundException.class);

            // verify
            verify(postRepository, times(1))
                  .findById(1L);
        }


        @DisplayName("글쓴이가 아니면 실패한다.")
        @Test
        void not_writer_fail() {
            // given
            Post post = PostFactory.imagePost(1L, 1L, "content", new ArrayList<>());

            PostDeleteReqeustDto requestDto = new PostDeleteReqeustDto(1L, 2L);

            given(postRepository.findById(requestDto.getPostId())).willReturn(Optional.of(post));

            // when then
            assertThatThrownBy(() -> postService.deletePost(requestDto))
                  .isInstanceOf(NotPostWriterException.class);

            // verify
            verify(postRepository, times(1))
                  .findById(1L);
        }
    }
}
