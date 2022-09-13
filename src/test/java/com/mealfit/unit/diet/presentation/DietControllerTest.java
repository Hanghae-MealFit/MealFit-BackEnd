package com.mealfit.unit.diet.presentation;

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
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mealfit.config.security.WithMockCustomUser;
import com.mealfit.diet.application.dto.request.DietListByDateRequestDto;
import com.mealfit.diet.application.dto.response.DietResponseByDateDto;
import com.mealfit.diet.application.dto.response.DietResponseDto;
import com.mealfit.diet.domain.Diet;
import com.mealfit.diet.domain.DietStatus;
import com.mealfit.diet.presentation.dto.request.DietUpdateRequest;
import com.mealfit.diet.presentation.dto.request.DietSaveRequest;
import com.mealfit.food.domain.Food;
import com.mealfit.unit.ControllerTest;
import com.mealfit.user.application.dto.response.UserNutritionGoalResponseDto;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class DietControllerTest extends ControllerTest {

    private static final String COMMON_API_ADDRESS = "/api/diet";

    @DisplayName("[POST] /api/diet 인 saveDiet()는")
    @Nested
    class Testing_saveFood {

        @WithMockCustomUser
        @DisplayName("완전한 정보를 입력했을 때 성공한다.")
        @Test
        void saveDiet_success() throws Exception {

            // given
            DietSaveRequest request = new DietSaveRequest(1L, 150,
                  "BREAKFAST", LocalDate.of(2022, 9, 1));

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
                  .andDo(document("diet-saveDiet",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰.")),
                              headerWithName("refresh_token").description("리프레시 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰."))
                        ), requestFields(
                              fieldWithPath("foodId").type(Long.class).description("음식 ID"),
                              fieldWithPath("foodWeight").type(double.class).description("섭취 중량"),
                              fieldWithPath("status").type(String.class).description("아침, 점심, 저녁"),
                              fieldWithPath("date").type(LocalDate.class).description("날짜")
                        )));
        }
    }

    @DisplayName("[PUT] /api/diet 인 updateDiet()는")
    @Nested
    class Testing_updateDiet {

        @WithMockCustomUser
        @DisplayName("완전한 정보를 입력했을 때 성공한다.")
        @Test
        void updateDiet_success() throws Exception {

            // given
            DietUpdateRequest request = new DietUpdateRequest(1L, 1L, 150);

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
                  .andDo(document("diet-updateDiet",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰.")),
                              headerWithName("refresh_token").description("리프레시 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰."))
                        ), requestFields(
                              fieldWithPath("dietId").type(Long.class).description("식단 ID"),
                              fieldWithPath("foodId").type(Long.class).description("음식 ID"),
                              fieldWithPath("foodWeight").type(double.class).description("중량")
                        )));
        }
    }

    @DisplayName("[DELETE] /api/diet/{dietId} 인 deleteDiet()는")
    @Nested
    class Testing_deleteDiet {

        @WithMockCustomUser
        @DisplayName("완전한 정보를 입력했을 때 성공한다.")
        @Test
        void deleteDiet_success() throws Exception {

            // when then
            mockMvc.perform(delete(COMMON_API_ADDRESS + "/{dietId}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .with(csrf().asHeader())
                  )
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("diet-deleteDiet",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰.")),
                              headerWithName("refresh_token").description("리프레시 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰."))
                        ), pathParameters(
                              parameterWithName("dietId").description("식단 ID")
                        )));

        }
    }

    @DisplayName("[GET] /api/diet 인 getDiet()는")
    @Nested
    class Testing_getDiet {

        @WithMockCustomUser
        @DisplayName("해당 날의 식단 정보를 가져온다.")
        @Test
        void getDiet_success() throws Exception {

            // given

            DietResponseByDateDto responseDto = new DietResponseByDateDto(
                  List.of(new DietResponseDto(
                        new Diet(1L, 1L, DietStatus.BREAKFAST, 100, LocalDate.of(2022, 9, 1)),
                        new Food(1L, "사과", 100, 89, 20, 2, 0, "전국"))
                  ), new UserNutritionGoalResponseDto(2000, 200, 140, 50)
            );

            given(dietService.getDietListByDate(any(DietListByDateRequestDto.class))).willReturn(
                  responseDto);

            // when then
            mockMvc.perform(get(COMMON_API_ADDRESS)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .queryParam("date", "2022-09-01")
                        .with(csrf().asHeader())
                  )
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("diet-deleteDiet",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰.")),
                              headerWithName("refresh_token").description("리프레시 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰."))
                        ), requestParameters(
                              parameterWithName("date").description("날짜")
                        ), responseFields(
                              fieldWithPath("dietResponseDto[].dietId").type(Long.class)
                                    .description("식단 ID"),
                              fieldWithPath("dietResponseDto[].foodId").type(Long.class)
                                    .description("음식 ID"),
                              fieldWithPath("dietResponseDto[].foodName").type(String.class)
                                    .description("음식 명"),
                              fieldWithPath("dietResponseDto[].madeBy").type(String.class)
                                    .description("제조사"),
                              fieldWithPath("dietResponseDto[].dietStatus").type(String.class)
                                    .description("아침, 점심, 저녁"),
                              fieldWithPath("dietResponseDto[].kcal").type(double.class)
                                    .description("칼로리"),
                              fieldWithPath("dietResponseDto[].carbs").type(double.class)
                                    .description("탄수화물"),
                              fieldWithPath("dietResponseDto[].protein").type(double.class)
                                    .description("단백질"),
                              fieldWithPath("dietResponseDto[].fat").type(double.class)
                                    .description("지방"),
                              fieldWithPath("dietResponseDto[].foodWeight").type(double.class)
                                    .description("중량"),
                              fieldWithPath("todayNutrition.kcal").type(double.class)
                                    .description("오늘 섭취한 총 칼로리"),
                              fieldWithPath("todayNutrition.carbs").type(double.class)
                                    .description("오늘 섭취한 총 탄수화물"),
                              fieldWithPath("todayNutrition.protein").type(double.class)
                                    .description("오늘 섭취한 총 단백질"),
                              fieldWithPath("todayNutrition.fat").type(double.class)
                                    .description("오늘 섭취한 총 지방"),
                              fieldWithPath("userNutritionGoalResponse.kcal").type(double.class)
                                    .description("회원 목표 섭취 칼로리"),
                              fieldWithPath("userNutritionGoalResponse.carbs").type(double.class)
                                    .description("회원 목표 섭취 탄수화물"),
                              fieldWithPath("userNutritionGoalResponse.protein").type(double.class)
                                    .description("회원 목표 섭취 단백질"),
                              fieldWithPath("userNutritionGoalResponse.fat").type(double.class)
                                    .description("회원 목표 섭취 지방")
                        ))
                  );
        }
    }
}
