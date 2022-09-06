package com.mealfit.unit.post.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mealfit.config.security.WithMockCustomUser;
import com.mealfit.post.presentation.dto.response.PostResponse;
import com.mealfit.unit.ControllerTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.payload.JsonFieldType;

public class PostReadControllerTest extends ControllerTest {

    private static final String COMMON_API_ADDRESS = "/api/post";

    @DisplayName("[GET] /api/post?lastId={lastId} 인 readAll()는")
    @Nested
    class Testing_readAll {

        @DisplayName("존재하는 게시글 ID 입력 시 게시글 정보를 반환한다.")
        @WithMockCustomUser
        @Test
        void login_success() throws Exception {

            List<PostResponse> postResponseList = new ArrayList<>();

            for (long i = 1; i < 10; i++) {
                postResponseList.add(PostResponse.builder()
                      .postId(i)
                      .content("게시글 내용 " + i)
                      .images(List.of("http://github.com/image/" + i))
                      .nickname("사용자 " + i)
                      .profileImage("프로필 사진 " + i)
                      .view((int) i)
                      .like((int) i)
                      .liked(true)
                      .createdAt(LocalDateTime.now())
                      .updatedAt(LocalDateTime.now())
                      .build());
            }

            given(postReadService.getReadAll(any(Pageable.class), anyLong())).willReturn(
                  postResponseList);

            mockMvc.perform(get(COMMON_API_ADDRESS)
                        .queryParam("lastId", "10")
                        .with(csrf().asHeader())
                  )
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("post-readAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                              parameterWithName("lastId").description("마지막 게시글 ID")
                        ),
                        responseFields(
                              fieldWithPath("[].postId").type(JsonFieldType.NUMBER)
                                    .description("게시글 ID"),
                              fieldWithPath("[].nickname").type(JsonFieldType.STRING)
                                    .description("글쓴이 닉네임"),
                              fieldWithPath("[].profileImage").type(JsonFieldType.STRING)
                                    .description("글쓴이 프로필 사진 경로"),
                              fieldWithPath("[].content").type(JsonFieldType.STRING)
                                    .description("게시글 내용"),
                              fieldWithPath("[].like").type(JsonFieldType.NUMBER)
                                    .description("게시글 좋아요 수"),
                              fieldWithPath("[].view").type(JsonFieldType.NUMBER)
                                    .description("게시글 조회 수"),
                              fieldWithPath("[].images").type(JsonFieldType.ARRAY)
                                    .description("게시글 이미지 경로"),
                              fieldWithPath("[].liked").type(JsonFieldType.BOOLEAN)
                                    .description("게시글 좋아요 여부"),
                              fieldWithPath("[].createdAt").type(JsonFieldType.STRING)
                                    .description("게시글 게시일"),
                              fieldWithPath("[].updatedAt").type(JsonFieldType.STRING)
                                    .description("게시글 수정일")
                        )
                  ));

            verify(postReadService, times(1))
                  .getReadAll(any(Pageable.class), anyLong());
        }

    }

    @DisplayName("[GET] /api/post/{postId} 인 updatePost()는")
    @Nested
    class Testing_readOne {

        @DisplayName("존재하는 게시글 ID 입력 시 게시글 정보를 반환한다.")
        @WithMockCustomUser
        @Test
        void readOne_Success() throws Exception {

            // given
            PostResponse response = PostResponse.builder()
                  .postId(1L)
                  .content("게시글 내용")
                  .images(List.of("http://github.com/image/1"))
                  .nickname("사용자")
                  .profileImage("프로필 사진")
                  .view(0)
                  .like(0)
                  .liked(true)
                  .createdAt(LocalDateTime.now())
                  .updatedAt(LocalDateTime.now())
                  .build();

            given(postReadService.getReadOne(anyLong())).willReturn(response);

            mockMvc.perform(get(COMMON_API_ADDRESS + "/{postId}", 1)
                        .with(csrf().asHeader()))
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("post-readOne",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                              parameterWithName("postId").description("게시글 ID")
                        ),
                        responseFields(
                              fieldWithPath("postId").type(JsonFieldType.NUMBER)
                                    .description("게시글 ID"),
                              fieldWithPath("nickname").type(JsonFieldType.STRING)
                                    .description("글쓴이 닉네임"),
                              fieldWithPath("profileImage").type(JsonFieldType.STRING)
                                    .description("글쓴이 프로필 사진 경로"),
                              fieldWithPath("content").type(JsonFieldType.STRING)
                                    .description("게시글 내용"),
                              fieldWithPath("like").type(JsonFieldType.NUMBER)
                                    .description("게시글 좋아요 수"),
                              fieldWithPath("view").type(JsonFieldType.NUMBER)
                                    .description("게시글 조회 수"),
                              fieldWithPath("images").type(JsonFieldType.ARRAY)
                                    .description("게시글 이미지 경로"),
                              fieldWithPath("liked").type(JsonFieldType.BOOLEAN)
                                    .description("게시글 좋아요 여부"),
                              fieldWithPath("createdAt").type(JsonFieldType.STRING)
                                    .description("게시글 게시일"),
                              fieldWithPath("updatedAt").type(JsonFieldType.STRING)
                                    .description("게시글 수정일")
                        ))
                  );

            verify(postReadService, times(1))
                  .getReadOne(anyLong());
        }
    }
}
