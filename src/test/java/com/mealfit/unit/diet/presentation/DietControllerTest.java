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

    @DisplayName("[POST] /api/diet ??? saveDiet()???")
    @Nested
    class Testing_saveFood {

        @WithMockCustomUser
        @DisplayName("????????? ????????? ???????????? ??? ????????????.")
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
                              headerWithName(HttpHeaders.AUTHORIZATION).description("????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????.")),
                              headerWithName("refresh_token").description("???????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????."))
                        ), requestFields(
                              fieldWithPath("foodId").type(Long.class).description("?????? ID"),
                              fieldWithPath("foodWeight").type(double.class).description("?????? ??????"),
                              fieldWithPath("status").type(String.class).description("??????, ??????, ??????"),
                              fieldWithPath("date").type(LocalDate.class).description("??????")
                        )));
        }
    }

    @DisplayName("[PUT] /api/diet ??? updateDiet()???")
    @Nested
    class Testing_updateDiet {

        @WithMockCustomUser
        @DisplayName("????????? ????????? ???????????? ??? ????????????.")
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
                              headerWithName(HttpHeaders.AUTHORIZATION).description("????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????.")),
                              headerWithName("refresh_token").description("???????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????."))
                        ), requestFields(
                              fieldWithPath("dietId").type(Long.class).description("?????? ID"),
                              fieldWithPath("foodId").type(Long.class).description("?????? ID"),
                              fieldWithPath("foodWeight").type(double.class).description("??????")
                        )));
        }
    }

    @DisplayName("[DELETE] /api/diet/{dietId} ??? deleteDiet()???")
    @Nested
    class Testing_deleteDiet {

        @WithMockCustomUser
        @DisplayName("????????? ????????? ???????????? ??? ????????????.")
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
                              headerWithName(HttpHeaders.AUTHORIZATION).description("????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????.")),
                              headerWithName("refresh_token").description("???????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????."))
                        ), pathParameters(
                              parameterWithName("dietId").description("?????? ID")
                        )));

        }
    }

    @DisplayName("[GET] /api/diet ??? getDiet()???")
    @Nested
    class Testing_getDiet {

        @WithMockCustomUser
        @DisplayName("?????? ?????? ?????? ????????? ????????????.")
        @Test
        void getDiet_success() throws Exception {

            // given

            DietResponseByDateDto responseDto = new DietResponseByDateDto(
                  List.of(new DietResponseDto(
                        new Diet(1L, 1L, DietStatus.BREAKFAST, 100, LocalDate.of(2022, 9, 1)),
                        new Food(1L, "??????", 100, 89, 20, 2, 0, "??????"))
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
                  .andDo(document("diet-showDiet",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????.")),
                              headerWithName("refresh_token").description("???????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????."))
                        ), requestParameters(
                              parameterWithName("date").description("??????")
                        ), responseFields(
                              fieldWithPath("dietResponseDto[].dietId").type(Long.class)
                                    .description("?????? ID"),
                              fieldWithPath("dietResponseDto[].foodId").type(Long.class)
                                    .description("?????? ID"),
                              fieldWithPath("dietResponseDto[].foodName").type(String.class)
                                    .description("?????? ???"),
                              fieldWithPath("dietResponseDto[].madeBy").type(String.class)
                                    .description("?????????"),
                              fieldWithPath("dietResponseDto[].dietStatus").type(String.class)
                                    .description("??????, ??????, ??????"),
                              fieldWithPath("dietResponseDto[].kcal").type(double.class)
                                    .description("?????????"),
                              fieldWithPath("dietResponseDto[].carbs").type(double.class)
                                    .description("????????????"),
                              fieldWithPath("dietResponseDto[].protein").type(double.class)
                                    .description("?????????"),
                              fieldWithPath("dietResponseDto[].fat").type(double.class)
                                    .description("??????"),
                              fieldWithPath("dietResponseDto[].foodWeight").type(double.class)
                                    .description("??????"),
                              fieldWithPath("todayNutrition.kcal").type(double.class)
                                    .description("?????? ????????? ??? ?????????"),
                              fieldWithPath("todayNutrition.carbs").type(double.class)
                                    .description("?????? ????????? ??? ????????????"),
                              fieldWithPath("todayNutrition.protein").type(double.class)
                                    .description("?????? ????????? ??? ?????????"),
                              fieldWithPath("todayNutrition.fat").type(double.class)
                                    .description("?????? ????????? ??? ??????"),
                              fieldWithPath("userNutritionGoalResponse.kcal").type(double.class)
                                    .description("?????? ?????? ?????? ?????????"),
                              fieldWithPath("userNutritionGoalResponse.carbs").type(double.class)
                                    .description("?????? ?????? ?????? ????????????"),
                              fieldWithPath("userNutritionGoalResponse.protein").type(double.class)
                                    .description("?????? ?????? ?????? ?????????"),
                              fieldWithPath("userNutritionGoalResponse.fat").type(double.class)
                                    .description("?????? ?????? ?????? ??????")
                        ))
                  );
        }
    }
}
