package com.mealfit.unit.comment.presentation;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mealfit.comment.application.dto.request.CommentLikeRequestDto;
import com.mealfit.comment.application.dto.request.CommentSaveRequestDto;
import com.mealfit.comment.domain.Comment;
import com.mealfit.comment.presentation.dto.request.CommentSaveRequest;
import com.mealfit.comment.presentation.dto.request.CommentUpdateRequest;
import com.mealfit.comment.presentation.dto.response.CommentResponse;
import com.mealfit.config.security.WithMockCustomUser;
import com.mealfit.unit.ControllerTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class CommentControllerTest extends ControllerTest {

    private static final String COMMON_API_ADDRESS = "/api/post";

    @DisplayName("[POST] /{postId}/comment 인 createComment()는")
    @Nested
    class Testing_createComment {

        @DisplayName("로그인시")
        @Nested
        class Context_Login {

            @DisplayName("모든 값이 정상적이라면 성공한다.")
            @WithMockCustomUser
            @Test
            void createComment_success() throws Exception {

                // given
                CommentSaveRequest request = new CommentSaveRequest("댓글 내용입니다.");

                CommentSaveRequestDto requestDto = new CommentSaveRequestDto(
                      "댓글 내용입니다.", 1L, 1L);

                CommentResponse response = new CommentResponse(new Comment("댓글 내용입니다.", 1L),
                      "닉네임입니다", "https://github.com/profileImage/1.jpeg", true);

                given(commentService.createComment(requestDto)).willReturn(response);

                // when
                mockMvc.perform(post(COMMON_API_ADDRESS + "/{postId}/comment", 1)
                            .with(csrf().asHeader())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsBytes(request))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                            .header("refresh_token", "Bearer refresh_token")
                      )
                      .andExpect(status().isOk())
                      .andDo(print())
                      .andDo(document("comment-createComment",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                  headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                                        .attributes(key("constraints").value("Bearer 토큰.")),
                                  headerWithName("refresh_token").description("리프레시 토큰")
                                        .attributes(key("constraints").value("Bearer 토큰."))),
                            pathParameters(
                                  parameterWithName("postId").description("게시글 ID")
                            ),
                            requestFields(
                                  fieldWithPath("content").description("댓글 내용")
                            )
                      ));
            }

            @DisplayName("내용이 비어있다면 400 Error 발생")
            @WithMockCustomUser
            @Test
            void createComment_no_content_fail() throws Exception {
                // when
                mockMvc.perform(post(COMMON_API_ADDRESS + "/{postId}/comment", 1)
                            .with(csrf().asHeader())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                      )
                      .andExpect(status().isInternalServerError());
            }
        }

        @DisplayName("로그인을 하지 않았다면.")
        @Nested
        class Context_NotLogin {

            @DisplayName("로그인 요청이 발생한다.")
            @Test
            void createComment_not_login_fail() throws Exception {

                mockMvc.perform(post(COMMON_API_ADDRESS + "/{postId}/comment", 1)
                            .with(csrf().asHeader())
                      )
                      .andExpect(status().is3xxRedirection());
            }
        }
    }

    @DisplayName("[DELETE] /comment/{commentId} 인 deleteComment()는")
    @Nested
    class Testing_deleteComment {

        @DisplayName("로그인시")
        @Nested
        class Context_Login {

            @DisplayName("모든 값이 정상적이고 본인이 그 댓글을 썻다면 삭제에 성공한다.")
            @WithMockCustomUser
            @Test
            void deleteComment_success() throws Exception {

                // when
                mockMvc.perform(delete(COMMON_API_ADDRESS + "/comment/{commentId}", 1)
                            .with(csrf().asHeader())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                            .header("refresh_token", "Bearer refresh_token")
                      )
                      .andExpect(status().isOk())
                      .andDo(print())
                      .andDo(document("comment-deleteComment",
                            preprocessRequest(prettyPrint()),
                            requestHeaders(
                                  headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                                        .attributes(key("constraints").value("Bearer 토큰.")),
                                  headerWithName("refresh_token").description("리프레시 토큰")
                                        .attributes(key("constraints").value("Bearer 토큰."))),
                            pathParameters(
                                  parameterWithName("commentId").description("댓글 ID")
                            )
                      ));
            }
        }

        @DisplayName("로그인을 하지 않았다면.")
        @Nested
        class Context_NotLogin {

            @DisplayName("로그인 요청이 발생한다.")
            @Test
            void deleteComment_not_login_fail() throws Exception {

                mockMvc.perform(delete(COMMON_API_ADDRESS + "/comment/{commentId}", 1)
                            .with(csrf().asHeader())
                      )
                      .andExpect(status().is3xxRedirection());
            }
        }
    }

    @DisplayName("[PUT] /comment/{commentId} 인 updateComment()는")
    @Nested
    class Testing_updateComment {

        @DisplayName("로그인시")
        @Nested
        class Context_Login {

            @DisplayName("모든 값이 정상적이고 본인이 그 댓글을 썻다면 수정에 성공한다.")
            @WithMockCustomUser
            @Test
            void updateComment_success() throws Exception {

                CommentUpdateRequest request = new CommentUpdateRequest("수정할 내용 입니다.");

                // when
                mockMvc.perform(put(COMMON_API_ADDRESS + "/comment/{commentId}", 1)
                            .with(csrf().asHeader())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsBytes(request))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                            .header("refresh_token", "Bearer refresh_token")
                      )
                      .andExpect(status().isOk())
                      .andDo(print())
                      .andDo(document("comment-updateComment",
                            preprocessRequest(prettyPrint()),
                            requestHeaders(
                                  headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                                        .attributes(key("constraints").value("Bearer 토큰.")),
                                  headerWithName("refresh_token").description("리프레시 토큰")
                                        .attributes(key("constraints").value("Bearer 토큰."))),
                            pathParameters(
                                  parameterWithName("commentId").description("댓글 ID")
                            ),
                            requestFields(
                                  fieldWithPath("content").type(String.class)
                                        .description("수정할 댓글 내용")
                            )
                      ));
            }

            @DisplayName("내용이 없다면 400 Error이 발생한다.")
            @WithMockCustomUser
            @Test
            void updateComment_no_content_fail() throws Exception {

                // when
                mockMvc.perform(put(COMMON_API_ADDRESS + "/comment/{commentId}", 1)
                            .with(csrf().asHeader())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                      )
                      .andExpect(status().isInternalServerError());
            }
        }

        @DisplayName("로그인을 하지 않았다면.")
        @Nested
        class Context_NotLogin {

            @DisplayName("로그인 요청이 발생한다.")
            @Test
            void deleteComment_not_login_fail() throws Exception {

                mockMvc.perform(put(COMMON_API_ADDRESS + "/comment/{commentId}", 1)
                            .with(csrf().asHeader())
                      )
                      .andExpect(status().is3xxRedirection());
            }
        }
    }

    @DisplayName("[GET] /{postId}/comment 인 listComment()는")
    @Nested
    class Testing_listComment {

        @DisplayName("로그인시")
        @Nested
        class Context_Login {

            @DisplayName("본인의 좋아요 여부를 체크하면서 댓글 목록을 가져온다.")
            @WithMockCustomUser
            @Test
            void listComment_success() throws Exception {

                // given
                List<CommentResponse> responseList = new ArrayList<>();

                for (long i = 0; i < 5; i++) {
                    responseList.add(
                          new CommentResponse(new Comment(1L, "내용", (int) i, 1L, 1L), "닉네임" + i,
                                "profileImage" + i,
                                true));
                }

                given(commentService.getCommentList(1L, 1L)).willReturn(responseList);

                // when
                mockMvc.perform(get(COMMON_API_ADDRESS + "/{postId}/comment", 1)
                            .with(csrf().asHeader())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                            .header("refresh_token", "Bearer refresh_token")
                      )
                      .andExpect(status().isOk())
                      .andDo(print())
                      .andDo(document("comment-listComment",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                  parameterWithName("postId").description("게시글 ID")
                            ),
                            requestHeaders(
                                  headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                                        .attributes(key("constraints").value("Bearer 토큰.")),
                                  headerWithName("refresh_token").description("리프레시 토큰")
                                        .attributes(key("constraints").value("Bearer 토큰."))),
                            responseFields(
                                  fieldWithPath("comments[].commentId").type(Long.class)
                                        .description("댓글 ID"),
                                  fieldWithPath("comments[].postId").type(Long.class)
                                        .description("게시글 ID"),
                                  fieldWithPath("comments[].content").type(String.class)
                                        .description("게시글 내용"),
                                  fieldWithPath("comments[].userDto.nickname").type(String.class)
                                        .description("작성자 닉네임"),
                                  fieldWithPath("comments[].userDto.profileImage").type(
                                              String.class)
                                        .description("작성자 프로필사진"),
                                  fieldWithPath("comments[].like").type(int.class)
                                        .description("좋아요 수"),
                                  fieldWithPath("comments[].liked").type(boolean.class)
                                        .description("로그인한 회원의 좋아요 여부")
                            )
                      ));
            }
        }

        @DisplayName("로그인을 하지 않았다면.")
        @Nested
        class Context_NotLogin {

            @DisplayName("좋아요 여부는 모두 false 이면서 댓글 목록을 가져온다.")
            @WithMockCustomUser
            @Test
            void listComment_success() throws Exception {

                // given
                List<CommentResponse> responseList = new ArrayList<>();

                for (long i = 0; i < 5; i++) {
                    responseList.add(
                          new CommentResponse(new Comment(1L, "내용", (int) i, 1L, 1L), "닉네임" + i,
                                "profileImage" + i,
                                false));
                }

                given(commentService.getCommentList(1L, 1L)).willReturn(responseList);

                // when
                mockMvc.perform(get(COMMON_API_ADDRESS + "/{postId}/comment", 1)
                            .with(csrf().asHeader())
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                      .andExpect(status().isOk())
                      .andDo(print())
                      .andDo(document("comment-listComment",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                  parameterWithName("postId").description("게시글 ID")
                            ),
                            responseFields(
                                  fieldWithPath("comments[].commentId").type(Long.class)
                                        .description("댓글 ID"),
                                  fieldWithPath("comments[].postId").type(Long.class)
                                        .description("게시글 ID"),
                                  fieldWithPath("comments[].content").type(String.class)
                                        .description("게시글 내용"),
                                  fieldWithPath("comments[].userDto.nickname").type(String.class)
                                        .description("작성자 닉네임"),
                                  fieldWithPath("comments[].userDto.profileImage").type(
                                              String.class)
                                        .description("작성자 프로필사진"),
                                  fieldWithPath("comments[].like").type(int.class)
                                        .description("좋아요 수"),
                                  fieldWithPath("comments[].liked").type(boolean.class)
                                        .description("로그인한 회원의 좋아요 여부"))
                      ));
            }
        }
    }

    @DisplayName("[POST] /{postId}/comment 인 likeIt()는")
    @Nested
    class Testing_likeIt {

        @DisplayName("로그인시")
        @Nested
        class Context_Login {

            @DisplayName("본인의 좋아요 여부를 체크하면서 댓글 목록을 가져온다.")
            @WithMockCustomUser
            @Test
            void likeIt_success() throws Exception {

                // given
                CommentLikeRequestDto requestDto = new CommentLikeRequestDto(1L, 1L);

                given(commentService.saveLike(requestDto)).willReturn(true);

                // when
                mockMvc.perform(post(COMMON_API_ADDRESS + "/comment/{commentId}/likeIt", 1)
                            .with(csrf().asHeader())

                            .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                            .header("refresh_token", "Bearer refresh_token")
                      )
                      .andExpect(status().isOk())
                      .andDo(print())
                      .andDo(document("comment-likeIt",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                  parameterWithName("commentId").description("게시글 ID")
                            ),
                            requestHeaders(
                                  headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                                        .attributes(key("constraints").value("Bearer 토큰.")),
                                  headerWithName("refresh_token").description("리프레시 토큰")
                                        .attributes(key("constraints").value("Bearer 토큰.")))
                      ));
            }
        }

        @DisplayName("로그인을 하지 않았다면.")
        @Nested
        class Context_NotLogin {

            @DisplayName("좋아요는 불가능하다.")
            @Test
            void listComment_success() throws Exception {
                // given
                CommentLikeRequestDto requestDto = new CommentLikeRequestDto(1L, 1L);

                given(commentService.saveLike(requestDto)).willReturn(true);

                // when
                mockMvc.perform(post(COMMON_API_ADDRESS + "/comment/{commentId}/likeIt", 1)
                            .with(csrf().asHeader())

                            .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                            .header("refresh_token", "Bearer refresh_token")
                      )
                      .andExpect(status().is3xxRedirection());
            }
        }
    }
}