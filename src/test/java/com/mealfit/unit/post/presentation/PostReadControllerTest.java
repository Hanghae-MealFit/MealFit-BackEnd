package com.mealfit.unit.post.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mealfit.config.security.WithMockCustomUser;
import com.mealfit.post.application.dto.request.PostDetailRequestDto;
import com.mealfit.post.application.dto.request.PostListRequestDto;
import com.mealfit.post.presentation.dto.response.PostResponse;
import com.mealfit.unit.ControllerTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;

public class PostReadControllerTest extends ControllerTest {

    private static final String COMMON_API_ADDRESS = "/api/post";

    @DisplayName("[GET] /api/post?lastId={lastId} ??? readAll()???")
    @Nested
    class Testing_readAll {

        @DisplayName("???????????? ????????? ID ?????? ??? ????????? ????????? ????????????.")
        @WithMockCustomUser
        @Test
        void login_success() throws Exception {

            List<PostResponse> postResponseList = new ArrayList<>();

            for (long i = 1; i < 10; i++) {
                postResponseList.add(PostResponse.builder()
                      .postId(i)
                      .content("????????? ?????? " + i)
                      .images(List.of("http://github.com/image/" + i))
                      .nickname("????????? " + i)
                      .profileImage("????????? ?????? " + i)
                      .view((int) i)
                      .like((int) i)
                      .liked(true)
                      .createdAt(LocalDateTime.now())
                      .updatedAt(LocalDateTime.now())
                      .build());
            }

            given(postReadService.getReadAll(any(PostListRequestDto.class))).willReturn(
                  postResponseList);

            mockMvc.perform(get(COMMON_API_ADDRESS)
                        .queryParam("lastId", "10")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .with(csrf().asHeader())
                  )
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("post-readAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                              parameterWithName("lastId").description("????????? ????????? ID")
                        ),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????.")),
                              headerWithName("refresh_token").description("???????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????."))
                        ),
                        responseFields(
                              fieldWithPath("[].postId").type(JsonFieldType.NUMBER)
                                    .description("????????? ID"),
                              fieldWithPath("[].nickname").type(JsonFieldType.STRING)
                                    .description("????????? ?????????"),
                              fieldWithPath("[].profileImage").type(JsonFieldType.STRING)
                                    .description("????????? ????????? ?????? ??????"),
                              fieldWithPath("[].content").type(JsonFieldType.STRING)
                                    .description("????????? ??????"),
                              fieldWithPath("[].like").type(JsonFieldType.NUMBER)
                                    .description("????????? ????????? ???"),
                              fieldWithPath("[].view").type(JsonFieldType.NUMBER)
                                    .description("????????? ?????? ???"),
                              fieldWithPath("[].images").type(JsonFieldType.ARRAY)
                                    .description("????????? ????????? ??????"),
                              fieldWithPath("[].liked").type(JsonFieldType.BOOLEAN)
                                    .description("????????? ????????? ??????"),
                              fieldWithPath("[].createdAt").type(JsonFieldType.STRING)
                                    .description("????????? ?????????"),
                              fieldWithPath("[].updatedAt").type(JsonFieldType.STRING)
                                    .description("????????? ?????????")
                        )
                  ));

            verify(postReadService, times(1))
                  .getReadAll(any(PostListRequestDto.class));
        }

    }

    @DisplayName("[GET] /api/post/{postId} ??? updatePost()???")
    @Nested
    class Testing_readOne {

        @DisplayName("???????????? ????????? ID ?????? ??? ????????? ????????? ????????????.")
        @WithMockCustomUser
        @Test
        void readOne_Success() throws Exception {

            // given
            PostResponse response = PostResponse.builder()
                  .postId(1L)
                  .content("????????? ??????")
                  .images(List.of("http://github.com/image/1"))
                  .nickname("?????????")
                  .profileImage("????????? ??????")
                  .view(0)
                  .like(0)
                  .liked(true)
                  .createdAt(LocalDateTime.now())
                  .updatedAt(LocalDateTime.now())
                  .build();

            given(postReadService.getReadOne(any(PostDetailRequestDto.class))).willReturn(response);

            mockMvc.perform(get(COMMON_API_ADDRESS + "/{postId}", 1)
                        .with(csrf().asHeader())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token"))
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("post-readOne",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                              parameterWithName("postId").description("????????? ID")
                        ),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????.")),
                              headerWithName("refresh_token").description("???????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????."))
                        ),
                        responseFields(
                              fieldWithPath("postId").type(JsonFieldType.NUMBER)
                                    .description("????????? ID"),
                              fieldWithPath("nickname").type(JsonFieldType.STRING)
                                    .description("????????? ?????????"),
                              fieldWithPath("profileImage").type(JsonFieldType.STRING)
                                    .description("????????? ????????? ?????? ??????"),
                              fieldWithPath("content").type(JsonFieldType.STRING)
                                    .description("????????? ??????"),
                              fieldWithPath("like").type(JsonFieldType.NUMBER)
                                    .description("????????? ????????? ???"),
                              fieldWithPath("view").type(JsonFieldType.NUMBER)
                                    .description("????????? ?????? ???"),
                              fieldWithPath("images").type(JsonFieldType.ARRAY)
                                    .description("????????? ????????? ??????"),
                              fieldWithPath("liked").type(JsonFieldType.BOOLEAN)
                                    .description("????????? ????????? ??????"),
                              fieldWithPath("createdAt").type(JsonFieldType.STRING)
                                    .description("????????? ?????????"),
                              fieldWithPath("updatedAt").type(JsonFieldType.STRING)
                                    .description("????????? ?????????")
                        ))
                  );

            verify(postReadService, times(1))
                  .getReadOne(any(PostDetailRequestDto.class));
        }
    }
}
