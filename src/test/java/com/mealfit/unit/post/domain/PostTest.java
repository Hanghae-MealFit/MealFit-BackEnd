package com.mealfit.unit.post.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mealfit.common.factory.PostFactory;
import com.mealfit.exception.user.UserNotFoundException;
import com.mealfit.post.domain.Post;
import com.mealfit.post.domain.PostImage;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("PostTest - 게시글 도메인 테스트")
public class PostTest {

    @DisplayName("settingUserInfo() 메서드는")
    @Nested
    class Testing_settingUserInfo {

        @DisplayName("회원 ID를 입력하는 경우 성공적으로 변경한다.")
        @Test
        void settingUserInfo_success() {
            // given
            Post testPost = PostFactory.simplePost(1, 0, "content");

            // when
            testPost.settingUserInfo(2L);

            // then
            assertEquals(1L, testPost.getId());
            assertEquals(2L, testPost.getUserId());
            assertEquals("content", testPost.getContent());
        }

        @DisplayName("회원 ID를 입력하는 않는 경우 NoUserException 발생.")
        @Test
        void settingUserInfo_failed() {
            // given
            Post testPost = PostFactory.simplePost(1, 0, "content");

            // when then
            assertThrows(UserNotFoundException.class, () -> testPost.settingUserInfo(null));
        }
    }

    @DisplayName("addPostImages() 메서드는")
    @Nested
    class Testing_addPostImages {
        @DisplayName("List<PostImage>를 전달하는 경우 성공적으로 저장한다.")
        @Test
        void addPostImages_success() {

            // given
            Post testPost = PostFactory.simplePost(1, 0, "content");
            PostImage postImage1 = new PostImage("https://github.com/testImage1.jpeg");
            PostImage postImage2 = new PostImage("https://github.com/testImage2.jpeg");

            // when
            testPost.addPostImages(List.of(postImage1, postImage2));

            // then
            assertThat(testPost.getImages()).contains(postImage1, postImage2);
            assertThat(testPost.getImages()).hasSize(2);
        }
    }


    @DisplayName("replacePostImages() 메서드는")
    @Nested
    class Testing_replacePostImages {
        @DisplayName("List<PostImage>를 전달하는 경우 성공적으로 저장한다.")
        @Test
        void replacePostImages_empty_success() {

            // given
            Post testPost = PostFactory.simplePost(1, 0, "content");
            PostImage postImage1 = new PostImage("https://github.com/testImage.jpeg");
            PostImage postImage2 = new PostImage("https://github.com/testImage.jpeg");

            // when
            testPost.replacePostImages(List.of(postImage1, postImage2));

            // then
            assertThat(testPost.getImages()).contains(postImage1, postImage2);
            assertThat(testPost.getImages()).hasSize(2);
        }

        @DisplayName("List<PostImage>를 전달하는 경우 성공적으로 저장한다.")
        @Test
        void replacePostImages_replace_success() {

            Post testPost = PostFactory.simplePost(1, 0, "content");
            PostImage postImage1 = new PostImage("https://github.com/testImage1.jpeg");
            PostImage postImage2 = new PostImage("https://github.com/testImage2.jpeg");

            testPost.addPostImages(List.of(postImage1, postImage2));

            PostImage postImage3 = new PostImage("https://github.com/testImage3.jpeg");
            PostImage postImage4 = new PostImage("https://github.com/testImage4.jpeg");

            testPost.replacePostImages(List.of(postImage3, postImage4));

            assertThat(testPost.getImages()).doesNotContain(postImage1, postImage2);
            assertThat(testPost.getImages()).contains(postImage3, postImage4);
            assertThat(testPost.getImages()).hasSize(2);
        }
    }

    @DisplayName("updateContent() 메서드는")
    @Nested
    class Testing_updateContent {
        @DisplayName("Content를 전달하면 성공적으로 저장한다.")
        @Test
        void updateContent_success(){
            // given
            Post testPost = PostFactory.simplePost(1, 0, "content");

            // when
            String newContent = "new content";

            testPost.updateContent(newContent);

            // then
            assertEquals(newContent, testPost.getContent());
        }


    }

    @DisplayName("getImageUrls() 메서드는")
    @Nested
    class Testing_getImageUrls {

        @DisplayName("가지고 있는 이미지 경로 리스트를 반환한다.")
        @Test
        void getImageUrls_success(){
            // given
            Post testPost = PostFactory.simplePost(1, 0, "content");
            PostImage postImage1 = new PostImage("https://github.com/testImage1.jpeg");
            PostImage postImage2 = new PostImage("https://github.com/testImage2.jpeg");

            // when
            testPost.addPostImages(List.of(postImage1, postImage2));
            List<String> imageUrls = testPost.getImageUrls();

            // then
            assertThat(imageUrls).contains("https://github.com/testImage1.jpeg", "https://github.com/testImage2.jpeg");
            assertThat(imageUrls).hasSize(2);
        }
    }

    @DisplayName("equals() 메서드는")
    @Nested
    class Testing_equals {

        @DisplayName("같은 인스턴스의 경우 참이다.")
        @Test
        void same_instance_success(){
            // given
            Post testPost = PostFactory.simplePost(1, 0, "content");
            // when then
            assertEquals(testPost, testPost);
        }

        @DisplayName("POST ID 값만 같으면 참이다.")
        @Test
        void equals_postId_success(){
            // given
            Post testPost = PostFactory.simplePost(1, 0, "content");
            Post equalsPost = PostFactory.simplePost(1, 0, "content");
            Post notEqualsPost = PostFactory.simplePost(2, 0, "content");

            // when then
            assertEquals(testPost, equalsPost);
            assertNotEquals(testPost, notEqualsPost);
        }

        @DisplayName("null 이거나, post 클래스의 인스턴스가 아닌 경우 거짓이다.")
        @Test
        void null_or_not_instance_success(){
            // given
            Post testPost = PostFactory.simplePost(1, 0, "content");
            Post nullPost = null;
            String notPostInstance = "string_class";

            // when then
            assertNotEquals(testPost, nullPost);
            assertNotEquals(testPost, notPostInstance);
        }
    }
}
