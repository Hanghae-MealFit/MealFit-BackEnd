package com.mealfit.unit.bodyInfo.presentation;

import static org.mockito.ArgumentMatchers.any;
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

import com.mealfit.bodyInfo.application.dto.request.BodyInfoRequestDto;
import com.mealfit.bodyInfo.application.dto.request.BodyInfoSaveRequestDto;
import com.mealfit.bodyInfo.application.dto.response.BodyInfoResponseDto;
import com.mealfit.bodyInfo.domain.BodyInfo;
import com.mealfit.bodyInfo.presentation.dto.request.BodyInfoChangeRequest;
import com.mealfit.bodyInfo.presentation.dto.request.BodyInfoSaveRequest;
import com.mealfit.config.security.WithMockCustomUser;
import com.mealfit.unit.ControllerTest;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

public class BodyInfoControllerTest extends ControllerTest {

    private static final String COMMON_API_ADDRESS = "/api/bodyInfo";

    @DisplayName("[POST] /api/bodyInfo 인 saveBodyInfo()는")
    @Nested
    class Testing_saveBodyInfo {

        @WithMockCustomUser
        @DisplayName("로그인 + 완전한 정보를 입력했을 때 성공한다.")
        @Test
        void saveBodyInfo_success() throws Exception {

            // given
            BodyInfoSaveRequest request = new BodyInfoSaveRequest(80.0,
                  LocalDate.of(2022, 9, 1));

            given(bodyInfoService.saveBodyInfo(any(BodyInfoSaveRequestDto.class)))
                  .willReturn(1L);

            // when then
            mockMvc.perform(post(COMMON_API_ADDRESS)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .content(objectMapper.writeValueAsBytes(request))
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                  )
                  .andExpect(status().isCreated())
                  .andDo(print())
                  .andDo(document("bodyInfo-saveBodyInfo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰.")),
                              headerWithName("refresh_token").description("리프레시 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰."))
                        ), requestFields(
                              fieldWithPath("weight").type(double.class).description("입력 몸무게"),
                              fieldWithPath("savedDate").type(LocalDate.class).description("입력 날짜")
                        )));
        }

        @DisplayName("비로그인 시 실패한다.")
        @Test
        void saveBodyInfo_fail() throws Exception {

            // given
            BodyInfoSaveRequest request = new BodyInfoSaveRequest(80.0,
                  LocalDate.of(2022, 9, 1));

            given(bodyInfoService.saveBodyInfo(any(BodyInfoSaveRequestDto.class)))
                  .willReturn(1L);

            // when then
            mockMvc.perform(post(COMMON_API_ADDRESS)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .content(objectMapper.writeValueAsBytes(request))
                        .secure(true)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                  )
                  .andExpect(status().is3xxRedirection())
                  .andDo(print());
        }
    }

    @DisplayName("[PUT] /api/bodyInfo 인 changeBodyInfo()는")
    @Nested
    class Testing_changeBodyInfo {

        @WithMockCustomUser
        @DisplayName("로그인 + 완전한 정보를 입력했을 때 성공한다.")
        @Test
        void changeBodyInfo_success() throws Exception {

            // given
            BodyInfoChangeRequest request = new BodyInfoChangeRequest(1L, 80.0,
                  LocalDate.of(2022, 9, 1));

            // when then
            mockMvc.perform(put(COMMON_API_ADDRESS)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .content(objectMapper.writeValueAsBytes(request))
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                  )
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("bodyInfo-changeBodyInfo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰.")),
                              headerWithName("refresh_token").description("리프레시 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰."))
                        ), requestFields(
                              fieldWithPath("id").type(double.class).description("기록 ID"),
                              fieldWithPath("weight").type(double.class).description("입력 몸무게"),
                              fieldWithPath("savedDate").type(LocalDate.class).description("입력 날짜")
                        )));
        }

        @DisplayName("비로그인 시 실패한다.")
        @Test
        void changeBodyInfo_fail() throws Exception {

            // given
            BodyInfoChangeRequest request = new BodyInfoChangeRequest(1L, 80.0,
                  LocalDate.of(2022, 9, 1));

            // when then
            mockMvc.perform(put(COMMON_API_ADDRESS)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .content(objectMapper.writeValueAsBytes(request))
                        .secure(true)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                  )
                  .andExpect(status().is3xxRedirection())
                  .andDo(print());
        }
    }

    @DisplayName("[GET] /api/showBodyInfo/{bodyInfoId} 인 showBodyInfo()는")
    @Nested
    class Testing_showBodyInfo {

        @WithMockCustomUser
        @DisplayName("로그인시 자신의 특정 BodyInfo를 조회한다.")
        @Test
        void showBodyInfo_success() throws Exception {

            // given
            Long bodyInfoId = 1L;

            given(bodyInfoService.showBodyInfo(any(BodyInfoRequestDto.class)))
                  .willReturn(new BodyInfoResponseDto(
                        new BodyInfo(1L, 1L, 80.0, LocalDate.now())));

            // when then
            mockMvc.perform(get(COMMON_API_ADDRESS + "/{bodyInfoId}", bodyInfoId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                  )
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("bodyInfo-showBodyInfo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰.")),
                              headerWithName("refresh_token").description("리프레시 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰."))
                        ), pathParameters(
                              parameterWithName("bodyInfoId").description("BodyInfo ID")
                        ),
                        responseFields(
                              fieldWithPath("id").type(JsonFieldType.NUMBER)
                                    .description("BodyInfo ID"),
                              fieldWithPath("weight").type(JsonFieldType.NUMBER)
                                    .description("BodyInfo ID"),
                              fieldWithPath("savedDate").type(JsonFieldType.STRING)
                                    .description("BodyInfo ID")
                        )));
        }

        @DisplayName("비로그인 시 실패한다.")
        @Test
        void showBodyInfo_fail() throws Exception {

            // when then
            mockMvc.perform(get(COMMON_API_ADDRESS + "/{bodyInfo}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                  )
                  .andExpect(status().is3xxRedirection())
                  .andDo(print());
        }
    }

    @DisplayName("[GET] /api/bodyInfo 인 showBodyInfoList()는")
    @Nested
    class Testing_showBodyInfoList {

        @WithMockCustomUser
        @DisplayName("로그인 + 완전한 정보를 입력했을 때 성공한다.")
        @Test
        void saveBodyInfo_success() throws Exception {

            // given
            BodyInfoSaveRequest request = new BodyInfoSaveRequest(80.0,
                  LocalDate.of(2022, 9, 1));

            given(bodyInfoService.showBodyInfos(any(BodyInfoRequestDto.class)))
                  .willReturn(List.of(
                        new BodyInfoResponseDto(
                              new BodyInfo(1L, 1L, 80.0, LocalDate.of(2022, 9, 1))),
                        new BodyInfoResponseDto(
                              new BodyInfo(2L, 1L, 79.0, LocalDate.of(2022, 9, 2))),
                        new BodyInfoResponseDto(
                              new BodyInfo(3L, 1L, 78.0, LocalDate.of(2022, 9, 3)))
                  ));

            // when then
            mockMvc.perform(get(COMMON_API_ADDRESS)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                  )
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("bodyInfo-showBodyInfoList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰.")),
                              headerWithName("refresh_token").description("리프레시 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰."))
                        ), responseFields(
                              fieldWithPath("data[].id").type(long.class).description("BodyInfo ID"),
                              fieldWithPath("data[].weight").type(double.class).description("몸무게"),
                              fieldWithPath("data[].savedDate").type(LocalDate.class).description("저장 날짜")
                        )));
        }

        @DisplayName("비로그인 시 실패한다.")
        @Test
        void saveBodyInfo_fail() throws Exception {

            // given
            BodyInfoSaveRequest request = new BodyInfoSaveRequest(80.0,
                  LocalDate.of(2022, 9, 1));

            // when then
            mockMvc.perform(get(COMMON_API_ADDRESS)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .secure(true)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                  )
                  .andExpect(status().is3xxRedirection())
                  .andDo(print());
        }
    }

    @DisplayName("[DELETE] /api/bodyInfo/{bodyInfoId} 인 deleteBodyInfo()는")
    @Nested
    class Testing_deleteBodyInfo {

        @WithMockCustomUser
        @DisplayName("본인이 입력한 기록일 때 성공한다.")
        @Test
        void deleteBodyInfo_success() throws Exception {

            // when then
            mockMvc.perform(delete(COMMON_API_ADDRESS + "/{bodyInfoId}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                  )
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("bodyInfo-deleteBodyInfo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰.")),
                              headerWithName("refresh_token").description("리프레시 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰."))
                        ),
                        pathParameters(
                              parameterWithName("bodyInfoId").description("BodyInfo Id")
                        )));
        }
    }
}
