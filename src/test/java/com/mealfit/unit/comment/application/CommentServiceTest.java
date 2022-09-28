package com.mealfit.unit.comment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mealfit.comment.application.CommentService;
import com.mealfit.comment.application.dto.request.CommentDeleteRequestDto;
import com.mealfit.comment.application.dto.request.CommentLikeRequestDto;
import com.mealfit.comment.application.dto.request.CommentUpdateRequestDto;
import com.mealfit.comment.application.dto.request.CommentSaveRequestDto;
import com.mealfit.comment.domain.Comment;
import com.mealfit.comment.domain.CommentLike;
import com.mealfit.comment.domain.CommentLikeRepository;
import com.mealfit.comment.domain.CommentRepository;
import com.mealfit.comment.presentation.dto.response.CommentResponse;
import com.mealfit.common.factory.UserFactory;
import com.mealfit.config.security.WithMockCustomUser;
import com.mealfit.exception.authentication.UnAuthorizedUserException;
import com.mealfit.exception.comment.CommentNotFoundException;
import com.mealfit.exception.post.PostNotFoundException;
import com.mealfit.exception.user.UserNotFoundException;
import com.mealfit.post.domain.PostRepository;
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

@DisplayName("CommentServiceTest - 댓글 조회 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentLikeRepository commentLikeRepository;

    private final User testUser = UserFactory.basicUser(1L, "username");
    private final User wrongUser = UserFactory.basicUser(2L, "username");

    @DisplayName("createComment() 메서드는")
    @Nested
    class Testing_createComment {

        @DisplayName("로그인 + 모든 정보를 입력하면 댓글이 작성된다.")
        @Test
        void createComment_success() {
            // given
            CommentSaveRequestDto requestDto = new CommentSaveRequestDto("댓글입니다.",
                  1L, 1L);

            User user = UserFactory.basicUser(1L, "username", "nickname",
                  "https://github.com/profileImage1/jpeg");

            Comment comment = Comment.builder()
                  .id(1L)
                  .content("댓글입니다.")
                  .user(user)
                  .postId(1L)
                  .likeIt(0)
                  .build();



            given(postRepository.existsById(anyLong())).willReturn(true);
            given(commentRepository.save(any(Comment.class))).willReturn(comment);
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

            // when
            CommentResponse response = commentService.createComment(requestDto);

            // then
            assertThat(response.getCommentId()).isEqualTo(1L);
            assertThat(response.getContent()).isEqualTo(requestDto.getContent());
            assertThat(response.getUserDto().getNickname()).isEqualTo(
                  user.getUserProfile().getNickname());
            assertThat(response.getUserDto().getProfileImage()).isEqualTo(
                  user.getUserProfile().getProfileImage());
            assertThat(response.getLike()).isEqualTo(0);
            assertThat(response.getCommentId()).isEqualTo(1L);

            verify(postRepository, times(1)).existsById(anyLong());
            verify(commentRepository, times(1)).save(any(Comment.class));
            verify(userRepository, times(1)).findById(anyLong());
        }

        @DisplayName("비로그인 시 UserNotFoundException 을 반환한다.")
        @Test
        void createComment_not_login_fail() {
            // given
            CommentSaveRequestDto requestDto = new CommentSaveRequestDto("댓글입니다.",
                  1L, 1L);

            given(postRepository.existsById(anyLong())).willReturn(true);
            given(userRepository.findById(anyLong())).willReturn(Optional.empty());

            // when then
            assertThatThrownBy(() -> commentService.createComment(requestDto))
                  .isInstanceOf(UserNotFoundException.class);
        }

        @DisplayName("게시글이 없을 시 PostNotFoundException 을 반환한다.")
        @Test
        void createComment_post_not_found_fail() {
            // given
            CommentSaveRequestDto requestDto = new CommentSaveRequestDto("댓글입니다.",
                  1L, 1L);

            given(postRepository.existsById(anyLong())).willReturn(false);

            // when then
            assertThatThrownBy(() -> commentService.createComment(requestDto))
                  .isInstanceOf(PostNotFoundException.class);
        }
    }

    @DisplayName("updateComment() 메서드는")
    @Nested
    class Testing_updateComment {

        @DisplayName("존재하지 않는 Comment_ID 의 경우 CommentNotFoundException 이 발생한다.")
        @Test
        void updateComment_no_comment_fail() {

            // given
            CommentUpdateRequestDto requestDto = new CommentUpdateRequestDto("새로운 댓글", 1L, 1L);

            given(commentRepository.findById(anyLong())).willReturn(Optional.empty());

            // when then
            assertThatThrownBy(() -> commentService.updateComment(requestDto))
                  .isInstanceOf(CommentNotFoundException.class);
        }

        @DisplayName("댓글 작성자가 아닌 경우 CommentNotFoundException 이 발생한다.")
        @Test
        void updateComment_not_writer_fail() {

            // given
            Comment wrongComment = Comment.builder()
                  .id(1L)
                  .postId(1L)
                  .user(wrongUser)
                  .content("댓글입니다.")
                  .build();

            CommentUpdateRequestDto requestDto =
                  new CommentUpdateRequestDto("새로운 댓글", 1L, 1L);

            given(commentRepository.findById(anyLong())).willReturn(Optional.of(wrongComment));

            // when then
            assertThatThrownBy(() -> commentService.updateComment(requestDto))
                  .isInstanceOf(UnAuthorizedUserException.class);
        }

        @DisplayName("작성자가 맞고 모든 내용이 잘 입력되었다면 성공적으로 수정된다.")
        @Test
        void updateComment_success() {

            // given
            Comment comment = Comment.builder()
                  .id(1L)
                  .postId(1L)
                  .user(testUser)
                  .content("댓글입니다.")
                  .build();

            CommentUpdateRequestDto requestDto =
                  new CommentUpdateRequestDto("새로운 댓글", 1L, 1L);

            given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

            // when then
            commentService.updateComment(requestDto);

            verify(commentRepository, times(1))
                  .findById(anyLong());
        }
    }

    @DisplayName("deleteComment() 메서드는")
    @Nested
    class Testing_deleteComment {

        @DisplayName("존재하지 않는 Comment_ID 의 경우 CommentNotFoundException 이 발생한다.")
        @Test
        void deleteComment_no_comment_fail() {

            // given
            CommentDeleteRequestDto requestDto = new CommentDeleteRequestDto(1L, 1L);

            given(commentRepository.findById(anyLong())).willReturn(Optional.empty());

            // when then
            assertThatThrownBy(() -> commentService.deleteComment(requestDto))
                  .isInstanceOf(CommentNotFoundException.class);
        }

        @DisplayName("댓글 작성자가 아닌 경우 CommentNotFoundException 이 발생한다.")
        @Test
        void deleteComment_not_writer_fail() {

            // given
            Comment wrongComment = Comment.builder()
                  .id(1L)
                  .postId(1L)
                  .user(wrongUser)
                  .content("댓글입니다.")
                  .build();

            CommentDeleteRequestDto requestDto = new CommentDeleteRequestDto(1L, 1L);

            given(commentRepository.findById(anyLong())).willReturn(Optional.of(wrongComment));

            // when then
            assertThatThrownBy(() -> commentService.deleteComment(requestDto))
                  .isInstanceOf(UnAuthorizedUserException.class);
        }

        @DisplayName("작성자가 맞고 모든 내용이 잘 입력되었다면 성공적으로 삭제된다.")
        @Test
        void deleteComment_success() {

            // given
            Comment comment = Comment.builder()
                  .id(1L)
                  .postId(1L)
                  .user(testUser)
                  .content("댓글입니다.")
                  .build();

            CommentDeleteRequestDto requestDto = new CommentDeleteRequestDto(1L, 1L);

            given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

            // when then
            commentService.deleteComment(requestDto);

            verify(commentRepository, times(1))
                  .findById(anyLong());
        }
    }

    @DisplayName("getCommentList() 메서드는")
    @WithMockCustomUser
    @Nested
    class Testing_getCommentList {

        @DisplayName("게시글 리스트를 보여준다.")
        @Test
        void getCommentList_success() {

            // given
            Comment comment1 = Comment.builder()
                  .id(1L)
                  .postId(1L)
                  .user(testUser)
                  .content("댓글입니다.")
                  .build();

            Comment comment2 = Comment.builder()
                  .id(1L)
                  .postId(1L)
                  .user(testUser)
                  .content("댓글입니다.")
                  .build();

            List<Comment> commentList = List.of(comment1, comment2);

            User user = UserFactory.basicUser(1L, "username", "닉네임", "프로필사진");

            given(commentRepository.findByPostIdOrderByCreatedAt(anyLong())).willReturn(
                  commentList);
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

            // when
            List<CommentResponse> result = commentService.getCommentList(1L, 1L);

            // then
            assertThat(result).hasSize(2);
        }
    }

    @DisplayName("saveLike() 메서드에서")
    @Nested
    class Testing_saveLike {

        @DisplayName("좋아요는 ")
        @Nested
        class Context_like {

            @DisplayName("좋아요 내역이 없다면 실행된다.")
            @Test
            void saveLike_like_success() {
                // given
                CommentLikeRequestDto requestDto = new CommentLikeRequestDto(1L, 1L);

                Comment comment = Comment.builder()
                      .id(1L)
                      .content("댓글입니다.")
                      .user(testUser)
                      .postId(1L)
                      .likeIt(0)
                      .build();

                given(commentLikeRepository.findByCommentIdAndUserId(anyLong(), anyLong()))
                      .willReturn(Optional.empty());
                given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

                // when
                boolean likeOrUnlike = commentService.saveLike(requestDto);

                // then
                assertThat(likeOrUnlike).isEqualTo(true);
                verify(commentLikeRepository, times(1)).findByCommentIdAndUserId(anyLong(),
                      anyLong());
                verify(commentRepository, times(1)).findById(anyLong());
            }

            @DisplayName("만약 Comment가 없으면 실패한다.")
            @Test
            void saveLike_comment_not_found_fail() {
                // given
                CommentLikeRequestDto requestDto = new CommentLikeRequestDto(1L, 1L);

                given(commentLikeRepository.findByCommentIdAndUserId(anyLong(), anyLong()))
                      .willReturn(Optional.empty());
                given(commentRepository.findById(anyLong())).willReturn(Optional.empty());

                // when then
                assertThatThrownBy(() -> commentService.saveLike(requestDto))
                      .isInstanceOf(CommentNotFoundException.class);
            }
        }

        @DisplayName("좋아요 취소는")
        @Nested
        class Context_unlike {

            @DisplayName("좋아요 내역이 있다면 좋아요를 취소한다.")
            @Test
            void saveLike_unlike_success() {
                // given
                CommentLikeRequestDto requestDto = new CommentLikeRequestDto(1L, 1L);

                Comment comment = Comment.builder()
                      .id(1L)
                      .content("댓글입니다.")
                      .user(testUser)
                      .postId(1L)
                      .likeIt(0)
                      .build();

                CommentLike commentLike = CommentLike.builder()
                      .commentId(1L)
                      .userId(1L)
                      .build();

                given(commentLikeRepository.findByCommentIdAndUserId(anyLong(), anyLong()))
                      .willReturn(Optional.of(commentLike));
                given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

                // when
                boolean likeOrUnlike = commentService.saveLike(requestDto);

                // then
                assertThat(likeOrUnlike).isEqualTo(false);
                verify(commentLikeRepository, times(1)).findByCommentIdAndUserId(anyLong(),
                      anyLong());
                verify(commentRepository, times(1)).findById(anyLong());
            }

            @DisplayName("만약 Comment가 없으면 실패한다.")
            @Test
            void saveLike_comment_not_found_fail() {
                // given
                CommentLikeRequestDto requestDto = new CommentLikeRequestDto(1L, 1L);

                CommentLike commentLike = CommentLike.builder()
                      .commentId(1L)
                      .userId(1L)
                      .build();

                given(commentLikeRepository.findByCommentIdAndUserId(anyLong(), anyLong()))
                      .willReturn(Optional.of(commentLike));

                given(commentRepository.findById(anyLong())).willReturn(Optional.empty());

                // when then
                assertThatThrownBy(() -> commentService.saveLike(requestDto))
                      .isInstanceOf(CommentNotFoundException.class);
            }
        }
    }
}