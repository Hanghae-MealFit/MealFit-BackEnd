package com.mealfit.unit.post.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mealfit.common.factory.PostFactory;
import com.mealfit.common.factory.UserFactory;
import com.mealfit.exception.post.PostNotFoundException;
import com.mealfit.exception.user.UserNotFoundException;
import com.mealfit.post.application.PostReadService;
import com.mealfit.post.domain.Post;
import com.mealfit.post.domain.PostImage;
import com.mealfit.post.domain.PostReadRepository;
import com.mealfit.post.presentation.dto.response.PostResponse;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@DisplayName("PostReadServiceTest - 게시글 조회 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class PostReadServiceTest {

    @InjectMocks
    private PostReadService postReadService;

    @Mock
    private PostReadRepository postReadRepository;

    @Mock
    private UserRepository userRepository;

    @DisplayName("getReadOne() 메서드는")
    @Nested
    class Testing_getReadOne {

        @DisplayName("post Id를 제공받으면 post 애그리거트를 제공한다.")
        @Test
        void success() {
            // given
            Post post = PostFactory.imagePost(1L, 1L, "content",
                  List.of(new PostImage("https://github.com/testImage1.jpeg"),
                        new PostImage("https://github.com/testImage2.jpeg")));

            User user = UserFactory
                  .basicUser(1L, "user", "nickname", "https://github.com/profileImg.jpeg");

            given(postReadRepository.findById(anyLong())).willReturn(Optional.of(post));
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

            // when
            PostResponse response = postReadService.getReadOne(1L);

            // then
            assertEquals(response.getPostId(), post.getId());
            assertEquals(response.getContent(), post.getContent());
            assertThat(response.getImages()).usingRecursiveComparison().isEqualTo(
                  List.of("https://github.com/testImage1.jpeg",
                        "https://github.com/testImage2.jpeg"));
            assertEquals(response.getNickname(), user.getUserProfile().getNickname());
            assertEquals(response.getProfileImage(), user.getUserProfile().getProfileImage());
            assertEquals(response.getView(), post.getView());
            assertEquals(response.getLike(), post.getLikeIt());

            verify(postReadRepository, times(1)).findById(anyLong());
            verify(userRepository, times(1)).findById(anyLong());
        }

        @DisplayName("없는 user_ID 이면 UserNotFoundException 발생.")
        @Test
        void no_postId_fail() {
            // given
            Post post = null;

            given(postReadRepository.findById(anyLong())).willReturn(Optional.empty());

            // when then
            assertThrows(PostNotFoundException.class, () -> postReadService.getReadOne(1L));

            verify(postReadRepository, times(1)).findById(anyLong());
        }

        @DisplayName("없는 post_ID 이면 PostNotFoundException 발생.")
        @Test
        void no_userId_fail() {
            // given
            Post post = PostFactory.imagePost(1L, 1L, "content",
                  List.of(new PostImage("https://github.com/testImage1.jpeg"),
                        new PostImage("https://github.com/testImage2.jpeg")));

            given(postReadRepository.findById(anyLong())).willReturn(Optional.of(post));
            given(userRepository.findById(anyLong())).willReturn(Optional.empty());

            // when then
            assertThrows(UserNotFoundException.class, () -> postReadService.getReadOne(1L));

            verify(postReadRepository, times(1)).findById(anyLong());
            verify(userRepository, times(1)).findById(anyLong());
        }
    }

    @DisplayName("getReadAll() 메서드는")
    @Nested
    class Testing_getReadAll {

        @DisplayName("Pagable과 PostId 를 제공받으면 pagable 에 맞게 게시글들을 제공해준다.")
        @Test
        void success() {
            // given
            Post post1 = PostFactory.imagePost(1L, 1L, "content1",
                  List.of(new PostImage("https://github.com/testImage1.jpeg"),
                        new PostImage("https://github.com/testImage2.jpeg")));

            Post post2 = PostFactory.imagePost(2L, 1L, "content2",
                  List.of(new PostImage("https://github.com/testImage1.jpeg"),
                        new PostImage("https://github.com/testImage2.jpeg")));

            Post post3 = PostFactory.imagePost(3L, 1L, "content3",
                  List.of(new PostImage("https://github.com/testImage1.jpeg"),
                        new PostImage("https://github.com/testImage2.jpeg")));

            Post post4 = PostFactory.imagePost(4L, 1L, "content4",
                  List.of(new PostImage("https://github.com/testImage1.jpeg"),
                        new PostImage("https://github.com/testImage2.jpeg")));

            List<Post> postList = List.of(post1, post2, post3, post4);

            Pageable pageable = PageRequest.of(0, 8, Sort.by("id").descending());

            Page<Post> postPage = new PageImpl<>(postList);

            User user = UserFactory
                  .basicUser(1L, "username1", "nickname1", "https://github.com/profileImg.jpeg");

            given(postReadRepository.findAllByIdLessThan(anyLong(), any(Pageable.class))).willReturn(postPage);
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

            // when
            List<PostResponse> postResponseList = postReadService.getReadAll(pageable, 10L);

            // then
            List<String> imageUrls = List.of("https://github.com/testImage1.jpeg",
                  "https://github.com/testImage2.jpeg");

            for (int i = 1; i <= postResponseList.size(); i++) {
                PostResponse postResponse = postResponseList.get(i-1);
                System.out.println(postResponse);
                assertThat(postResponse.getPostId()).isEqualTo((long) i);
                assertThat(postResponse.getNickname()).isEqualTo(user.getUserProfile().getNickname());
                assertThat(postResponse.getProfileImage()).isEqualTo(user.getUserProfile().getProfileImage());
                assertThat(postResponse.getContent()).isEqualTo(postResponse.getContent());
                assertThat(postResponse.getImages()).usingRecursiveComparison().isEqualTo(imageUrls);
            }

            verify(postReadRepository, times(1))
                  .findAllByIdLessThan(anyLong(), any(Pageable.class));
            verify(userRepository, times(4))
                  .findById(anyLong());
        }

        @DisplayName("없는 post_ID 이면 PostNotFoundException 발생.")
        @Test
        void no_userId_fail() {
            // given
            Post post1 = PostFactory.imagePost(1L, 1L, "content1",
                  List.of(new PostImage("https://github.com/testImage1.jpeg"),
                        new PostImage("https://github.com/testImage2.jpeg")));

            Post post2 = PostFactory.imagePost(2L, 1L, "content2",
                  List.of(new PostImage("https://github.com/testImage1.jpeg"),
                        new PostImage("https://github.com/testImage2.jpeg")));

            Post post3 = PostFactory.imagePost(3L, 1L, "content3",
                  List.of(new PostImage("https://github.com/testImage1.jpeg"),
                        new PostImage("https://github.com/testImage2.jpeg")));

            Post post4 = PostFactory.imagePost(4L, 1L, "content4",
                  List.of(new PostImage("https://github.com/testImage1.jpeg"),
                        new PostImage("https://github.com/testImage2.jpeg")));

            List<Post> postList = List.of(post1, post2, post3, post4);

            Pageable pageable = PageRequest.of(0, 8, Sort.by("id").descending());

            Page<Post> postPage = new PageImpl<>(postList);

            given(postReadRepository.findAllByIdLessThan(anyLong(), any(Pageable.class)))
                  .willReturn(postPage);
            given(userRepository.findById(anyLong())).willReturn(Optional.empty());

            // when then
            assertThrows(UserNotFoundException.class,
                  () -> postReadService.getReadAll(pageable, 10L));

            verify(postReadRepository, times(1))
                  .findAllByIdLessThan(anyLong(), any(Pageable.class));
            verify(userRepository, times(1)).findById(anyLong());
        }
    }
}
