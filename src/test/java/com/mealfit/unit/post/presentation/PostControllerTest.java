package com.mealfit.unit.post.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mealfit.common.factory.PostFactory;
import com.mealfit.common.factory.UserFactory;
import com.mealfit.config.security.WithMockCustomUser;
import com.mealfit.post.application.dto.request.PostCreateRequestDto;
import com.mealfit.post.application.dto.request.PostDeleteReqeustDto;
import com.mealfit.post.application.dto.request.PostUpdateRequestDto;
import com.mealfit.post.domain.Post;
import com.mealfit.post.domain.PostImage;
import com.mealfit.post.presentation.dto.response.PostCUDResponse;
import com.mealfit.unit.ControllerTest;
import com.mealfit.user.domain.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;

public class PostControllerTest extends ControllerTest {

    private static final String COMMON_API_ADDRESS = "/api/post";
    private static final User testUser = UserFactory.basicUser(1L, "username");

    @DisplayName("[POST] /api/post ??? createPost()???")
    @Nested
    class Testing_createPost {

        @DisplayName("??? ????????? ??? ????????????.")
        @Test
        void not_login_fail() throws Exception {

            MockMultipartFile image = new MockMultipartFile("postImageList",
                  "<<imageFile>>".getBytes());

            mockMvc.perform(multipart(COMMON_API_ADDRESS)
                        .file(image)
                        .param("content", "????????? ????????? ???????????????.")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(csrf().asHeader())
                  )
                  .andExpect(status().is3xxRedirection());
        }

        @DisplayName("????????? ??? ????????????.")
        @WithMockCustomUser
        @Test
        void login_success() throws Exception {
            MockMultipartFile image = new MockMultipartFile("postImageList",
                  "<<imageFile>>".getBytes());

            Post savedPost = PostFactory.imagePost(1L,
                  testUser,
                  "????????? ????????? ???????????????.",
                  List.of(new PostImage("http://github.com/newImage")));

            given(postService.write(any(PostCreateRequestDto.class)))
                  .willReturn(new PostCUDResponse(savedPost));

            mockMvc.perform(multipart(COMMON_API_ADDRESS)
                        .file(image)
                        .param("content", "????????? ????????? ???????????????.")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .with(csrf().asHeader())
                  )
                  .andExpect(status().isCreated())
                  .andDo(print())
                  .andDo(document("post-createPost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????.")),
                              headerWithName("refresh_token").description("???????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????."))
                        ), requestParameters(
                              parameterWithName("content").optional().description("????????? ??????")
                        ), requestParts(
                              partWithName("postImageList").optional().description("????????? ??????")
                        ), responseFields(
                              fieldWithPath("postId").type(JsonFieldType.NUMBER)
                                    .description("????????? ID"),
                              fieldWithPath("content").type(JsonFieldType.STRING)
                                    .description("????????? ??????"),
                              fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                    .description("?????? ?????????"),
                              fieldWithPath("imageUrls").type(JsonFieldType.ARRAY)
                                    .description("????????? ????????? ??????")
                        )
                  ));

            verify(postService, times(1))
                  .write(any());
        }

        @DisplayName("???????????? ???????????? ?????? ?????? ????????????.")
        @WithMockCustomUser
        @Test
        void not_fill_content_fail() throws Exception {
            MockMultipartFile image = new MockMultipartFile("postImageList",
                  "<<imageFile>>".getBytes());

            mockMvc.perform(multipart(COMMON_API_ADDRESS)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .with(csrf().asHeader())
                  )
                  .andExpect(status().isBadRequest())
                  .andDo(print());
        }

        @DisplayName("???????????? ???????????? ?????? ?????? ????????????.")
        @WithMockCustomUser
        @Test
        void not_fill_image_fail() throws Exception {
            MockMultipartFile image = new MockMultipartFile("postImageList",
                  "<<imageFile>>".getBytes());

            mockMvc.perform(multipart(COMMON_API_ADDRESS)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .param("content", "????????? Post??? Content ?????????.")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .with(csrf().asHeader())
                  )
                  .andExpect(status().isBadRequest())
                  .andDo(print());
        }
    }

    @DisplayName("[PUT] /api/post/{postId} ??? updatePost()???")
    @Nested
    class Testing_updatePost {

        @DisplayName("??? ????????? ??? ????????????.")
        @Test
        void not_login_fail() throws Exception {

            MockMultipartFile image = new MockMultipartFile("postImageList",
                  "<<imageFile>>".getBytes());

            mockMvc.perform(multipart(COMMON_API_ADDRESS + "/{postId}", 1)
                        .file(image)
                        .param("content", "????????? ????????? ???????????????.")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(csrf().asHeader())
                  )
                  .andExpect(status().is3xxRedirection());
        }

        @DisplayName("????????? ??? ????????????.")
        @WithMockCustomUser
        @Test
        void login_success() throws Exception {
            MockMultipartFile image = new MockMultipartFile("postImageList",
                  "<<imageFile>>".getBytes());

            Post savedPost = PostFactory.imagePost(1L,
                  testUser,
                  "????????? ????????? ???????????????.",
                  List.of(new PostImage("http://github.com/newImage")));

            given(postService.updatePost(any(PostUpdateRequestDto.class)))
                  .willReturn(new PostCUDResponse(savedPost));

            mockMvc.perform(multipart(COMMON_API_ADDRESS + "/{postId}", 1)
                        .file(image)
                        .param("content", "????????? ????????? ???????????????.")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .with(csrf().asHeader())
                  )
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("post-updatePost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                              parameterWithName("postId").description("????????? ID")),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????.")),
                              headerWithName("refresh_token").description("???????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????."))
                        ), requestParameters(
                              parameterWithName("content").optional().description("????????? ??????")
                        ), requestParts(
                              partWithName("postImageList").optional().description("????????? ??????")
                        )
                  ));

            verify(postService, times(1))
                  .updatePost(any(PostUpdateRequestDto.class));
        }

    }

    @DisplayName("[DELETE] /api/post/{postId} ??? deletePost()???")
    @Nested
    class Testing_deletePost {

        @DisplayName("??? ????????? ??? ????????????.")
        @Test
        void not_login_fail() throws Exception {

            mockMvc.perform(delete(COMMON_API_ADDRESS + "/{postId}", 1)
                        .with(csrf().asHeader())
                  )
                  .andExpect(status().is3xxRedirection());
        }

        @DisplayName("????????? ??? ????????????.")
        @WithMockCustomUser
        @Test
        void login_success() throws Exception {

            given(postService.deletePost(any(PostDeleteReqeustDto.class))).willReturn(1L);

            mockMvc.perform(delete(COMMON_API_ADDRESS + "/{postId}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .with(csrf().asHeader())
                  )
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("post-deletePost",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                              parameterWithName("postId").description("????????? ID")),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????.")),
                              headerWithName("refresh_token").description("???????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????."))
                        )
                  ));

            verify(postService, times(1))
                  .deletePost(any());
        }

    }
}
