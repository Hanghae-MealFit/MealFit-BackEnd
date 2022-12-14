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
import com.mealfit.common.factory.UserFactory;
import com.mealfit.config.security.WithMockCustomUser;
import com.mealfit.unit.ControllerTest;
import com.mealfit.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class CommentControllerTest extends ControllerTest {

    private static final String COMMON_API_ADDRESS = "/api/post";

    private final User testUser = UserFactory.basicUser(1L, "username");
    private final User wrongUser = UserFactory.basicUser(2L, "username");

    @DisplayName("[POST] /{postId}/comment ??? createComment()???")
    @Nested
    class Testing_createComment {

        @DisplayName("????????????")
        @Nested
        class Context_Login {

            @DisplayName("?????? ?????? ?????????????????? ????????????.")
            @WithMockCustomUser
            @Test
            void createComment_success() throws Exception {

                // given
                CommentSaveRequest request = new CommentSaveRequest("?????? ???????????????.");

                CommentSaveRequestDto requestDto = new CommentSaveRequestDto(
                      "?????? ???????????????.", 1L, 1L);

                CommentResponse response = new CommentResponse(new Comment("?????? ???????????????.", 1L),
                      "??????????????????", "https://github.com/profileImage/1.jpeg", true);

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
                                  headerWithName(HttpHeaders.AUTHORIZATION).description("????????? ??????")
                                        .attributes(key("constraints").value("Bearer ??????.")),
                                  headerWithName("refresh_token").description("???????????? ??????")
                                        .attributes(key("constraints").value("Bearer ??????."))),
                            pathParameters(
                                  parameterWithName("postId").description("????????? ID")
                            ),
                            requestFields(
                                  fieldWithPath("content").description("?????? ??????")
                            )
                      ));
            }

            @DisplayName("????????? ??????????????? 400 Error ??????")
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

        @DisplayName("???????????? ?????? ????????????.")
        @Nested
        class Context_NotLogin {

            @DisplayName("????????? ????????? ????????????.")
            @Test
            void createComment_not_login_fail() throws Exception {

                mockMvc.perform(post(COMMON_API_ADDRESS + "/{postId}/comment", 1)
                            .with(csrf().asHeader())
                      )
                      .andExpect(status().is3xxRedirection());
            }
        }
    }

    @DisplayName("[DELETE] /comment/{commentId} ??? deleteComment()???")
    @Nested
    class Testing_deleteComment {

        @DisplayName("????????????")
        @Nested
        class Context_Login {

            @DisplayName("?????? ?????? ??????????????? ????????? ??? ????????? ????????? ????????? ????????????.")
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
                                  headerWithName(HttpHeaders.AUTHORIZATION).description("????????? ??????")
                                        .attributes(key("constraints").value("Bearer ??????.")),
                                  headerWithName("refresh_token").description("???????????? ??????")
                                        .attributes(key("constraints").value("Bearer ??????."))),
                            pathParameters(
                                  parameterWithName("commentId").description("?????? ID")
                            )
                      ));
            }
        }

        @DisplayName("???????????? ?????? ????????????.")
        @Nested
        class Context_NotLogin {

            @DisplayName("????????? ????????? ????????????.")
            @Test
            void deleteComment_not_login_fail() throws Exception {

                mockMvc.perform(delete(COMMON_API_ADDRESS + "/comment/{commentId}", 1)
                            .with(csrf().asHeader())
                      )
                      .andExpect(status().is3xxRedirection());
            }
        }
    }

    @DisplayName("[PUT] /comment/{commentId} ??? updateComment()???")
    @Nested
    class Testing_updateComment {

        @DisplayName("????????????")
        @Nested
        class Context_Login {

            @DisplayName("?????? ?????? ??????????????? ????????? ??? ????????? ????????? ????????? ????????????.")
            @WithMockCustomUser
            @Test
            void updateComment_success() throws Exception {

                CommentUpdateRequest request = new CommentUpdateRequest("????????? ?????? ?????????.");

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
                                  headerWithName(HttpHeaders.AUTHORIZATION).description("????????? ??????")
                                        .attributes(key("constraints").value("Bearer ??????.")),
                                  headerWithName("refresh_token").description("???????????? ??????")
                                        .attributes(key("constraints").value("Bearer ??????."))),
                            pathParameters(
                                  parameterWithName("commentId").description("?????? ID")
                            ),
                            requestFields(
                                  fieldWithPath("content").type(String.class)
                                        .description("????????? ?????? ??????")
                            )
                      ));
            }

            @DisplayName("????????? ????????? 400 Error??? ????????????.")
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

        @DisplayName("???????????? ?????? ????????????.")
        @Nested
        class Context_NotLogin {

            @DisplayName("????????? ????????? ????????????.")
            @Test
            void deleteComment_not_login_fail() throws Exception {

                mockMvc.perform(put(COMMON_API_ADDRESS + "/comment/{commentId}", 1)
                            .with(csrf().asHeader())
                      )
                      .andExpect(status().is3xxRedirection());
            }
        }
    }

    @DisplayName("[GET] /{postId}/comment ??? listComment()???")
    @Nested
    class Testing_listComment {

        @DisplayName("????????????")
        @Nested
        class Context_Login {

            @DisplayName("????????? ????????? ????????? ??????????????? ?????? ????????? ????????????.")
            @WithMockCustomUser
            @Test
            void listComment_success() throws Exception {

                // given
                List<CommentResponse> responseList = new ArrayList<>();

                for (long i = 0; i < 5; i++) {
                    responseList.add(
                          new CommentResponse(new Comment(1L, "??????", (int) i, 1L, testUser), "?????????" + i,
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
                                  parameterWithName("postId").description("????????? ID")
                            ),
                            requestHeaders(
                                  headerWithName(HttpHeaders.AUTHORIZATION).description("????????? ??????")
                                        .attributes(key("constraints").value("Bearer ??????.")),
                                  headerWithName("refresh_token").description("???????????? ??????")
                                        .attributes(key("constraints").value("Bearer ??????."))),
                            responseFields(
                                  fieldWithPath("comments[].commentId").type(Long.class)
                                        .description("?????? ID"),
                                  fieldWithPath("comments[].postId").type(Long.class)
                                        .description("????????? ID"),
                                  fieldWithPath("comments[].content").type(String.class)
                                        .description("????????? ??????"),
                                  fieldWithPath("comments[].userDto.nickname").type(String.class)
                                        .description("????????? ?????????"),
                                  fieldWithPath("comments[].userDto.profileImage").type(
                                              String.class)
                                        .description("????????? ???????????????"),
                                  fieldWithPath("comments[].like").type(int.class)
                                        .description("????????? ???"),
                                  fieldWithPath("comments[].liked").type(boolean.class)
                                        .description("???????????? ????????? ????????? ??????")
                            )
                      ));
            }
        }

        @DisplayName("???????????? ?????? ????????????.")
        @Nested
        class Context_NotLogin {

            @DisplayName("????????? ????????? ?????? false ????????? ?????? ????????? ????????????.")
            @WithMockCustomUser
            @Test
            void listComment_success() throws Exception {

                // given
                List<CommentResponse> responseList = new ArrayList<>();

                for (long i = 0; i < 5; i++) {
                    responseList.add(
                          new CommentResponse(new Comment(1L, "??????", (int) i, 1L, testUser), "?????????" + i,
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
                                  parameterWithName("postId").description("????????? ID")
                            ),
                            responseFields(
                                  fieldWithPath("comments[].commentId").type(Long.class)
                                        .description("?????? ID"),
                                  fieldWithPath("comments[].postId").type(Long.class)
                                        .description("????????? ID"),
                                  fieldWithPath("comments[].content").type(String.class)
                                        .description("????????? ??????"),
                                  fieldWithPath("comments[].userDto.nickname").type(String.class)
                                        .description("????????? ?????????"),
                                  fieldWithPath("comments[].userDto.profileImage").type(
                                              String.class)
                                        .description("????????? ???????????????"),
                                  fieldWithPath("comments[].like").type(int.class)
                                        .description("????????? ???"),
                                  fieldWithPath("comments[].liked").type(boolean.class)
                                        .description("???????????? ????????? ????????? ??????"))
                      ));
            }
        }
    }

    @DisplayName("[POST] /{postId}/comment ??? likeIt()???")
    @Nested
    class Testing_likeIt {

        @DisplayName("????????????")
        @Nested
        class Context_Login {

            @DisplayName("????????? ????????? ????????? ??????????????? ?????? ????????? ????????????.")
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
                                  parameterWithName("commentId").description("????????? ID")
                            ),
                            requestHeaders(
                                  headerWithName(HttpHeaders.AUTHORIZATION).description("????????? ??????")
                                        .attributes(key("constraints").value("Bearer ??????.")),
                                  headerWithName("refresh_token").description("???????????? ??????")
                                        .attributes(key("constraints").value("Bearer ??????.")))
                      ));
            }
        }

        @DisplayName("???????????? ?????? ????????????.")
        @Nested
        class Context_NotLogin {

            @DisplayName("???????????? ???????????????.")
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