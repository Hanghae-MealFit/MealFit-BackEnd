package com.mealfit.unit.post.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mealfit.common.factory.PostFactory;
import com.mealfit.common.factory.UserFactory;
import com.mealfit.common.storageService.StorageService;
import com.mealfit.post.domain.Post;
import com.mealfit.post.dto.PostCUDResponseDto;
import com.mealfit.post.dto.PostRequestDto;
import com.mealfit.post.repository.PostRepository;
import com.mealfit.post.service.PostService;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserStatus;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

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

        @DisplayName("로그인이 되어야")
        @Nested
        class Context_Login {

            @DisplayName("글을 쓸 수 있다.")
            @Test
            void createPost_success() {

                // given
                User testUser = UserFactory.createMockLocalUser(1L, "username", "qwe123!!",
                      "nickname1", "test@gmail.com", UserStatus.NORMAL);

                Post post = PostFactory.createPost(testUser);
                PostRequestDto mockPostRequestDto = PostFactory.createMockPostRequestDto();

                given(postRepository.save(any(Post.class))).willReturn(post);
                given(storageService.uploadMultipartFile(anyList(), anyString()))
                      .willReturn(convertImageToUrl(mockPostRequestDto));

                // when
                PostCUDResponseDto savedPost = postService.createPost(mockPostRequestDto, testUser);

                verify(postRepository, times(1))
                      .save(any(Post.class));

                verify(storageService, times(1))
                      .uploadMultipartFile(anyList(), anyString());
            }
        }

        private List<String> convertImageToUrl(PostRequestDto postRequestDto) {
            return postRequestDto.getPostImageList().stream()
                  .map(MultipartFile::getOriginalFilename)
                  .map(name -> String.format("http://testImage.t/%s", name))
                  .collect(Collectors.toList());
        }

        @DisplayName("로그인이 되어있지 않으면")
        @Nested
        class Context_Not_Login {

            @DisplayName("글을 쓸 수 없다.")
            @Test
            void createPost_unSuccess() {

                // given
                PostRequestDto mockPostRequestDto = PostFactory.createMockPostRequestDto();

                // when then
                assertThatThrownBy(() -> postService.createPost(mockPostRequestDto, null))
                      .isInstanceOf(NullPointerException.class);
//                      .extracting("errorCode")
//                      .isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
            }
        }
    }
}
