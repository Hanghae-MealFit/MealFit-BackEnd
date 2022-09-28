package com.mealfit.unit.user.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.mealfit.common.factory.PostFactory;
import com.mealfit.common.factory.UserFactory;
import com.mealfit.post.domain.Post;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("UserTest- 회원 도메인 테스트")
public class UserTest {

    @DisplayName("changeProfile() 메서드는")
    @Nested
    class Testing_changeProfile {

        @DisplayName("회원 정보를 입력하면 변경된다.")
        @Test
        void changeProfile_success(){
            User user = UserFactory.basicUser(1L, "aaa");
            user.changeUserStatus(UserStatus.NORMAL);
            user.changeNickname("new nickname");
            user.changeProfileImage("https://github.com/newImage");

            assertEquals(UserStatus.NORMAL, user.getUserStatusInfo().getUserStatus());
            assertEquals("new nickname", user.getUserProfile().getNickname());
            assertEquals("https://github.com/newImage", user.getUserProfile().getProfileImage());
        }
    }

    @DisplayName("equals() 메서드는")
    @Nested
    class Testing_equals {

        @DisplayName("id만 같으면 같은 엔티티로 취급한다.")
        @Test
        void equals_same_id_success(){
            User user1 = UserFactory.basicUser(1L, "aaa");
            User user2 = UserFactory.basicUser(1L, "bbb");

            assertEquals(user1, user2);
        }

        @DisplayName("id가 다르면 다른 엔티티로 취급한다.")
        @Test
        void equals_different_id_fail(){
            User user1 = UserFactory.basicUser(1L, "aaa");
            User user2 = UserFactory.basicUser(2L, "aaa");

            assertNotEquals(user1, user2);
        }

        @DisplayName("같은 오브젝트 같은 엔티티로 취급한다.")
        @Test
        void equals_same_object_success(){
            User user1 = UserFactory.basicUser(1L, "aaa");

            assertEquals(user1, user1);
        }

        @DisplayName("다른 오브젝트인 경우 같지 않다.")
        @Test
        void equals_different_object_fail(){
            User user1 = UserFactory.basicUser(1L, "aaa");
            User user2 = null;
            Post post1 = PostFactory.simplePost(1L, null, "content");

            assertNotEquals(user1, post1);
            assertNotEquals(user1, null);
            assertNotEquals(user1, user2);
        }
    }
}
