package com.mealfit.unit.food.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mealfit.config.security.WithMockCustomUser;
import com.mealfit.food.application.dto.request.FoodRequestDto;
import com.mealfit.food.domain.Food;
import com.mealfit.food.presentation.dto.request.FoodSaveRequest;
import com.mealfit.food.presentation.dto.response.FoodInfoResponse;
import com.mealfit.unit.ControllerTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class FoodControllerTest extends ControllerTest {

    private static final String COMMON_API_ADDRESS = "/api/food";

    @DisplayName("[GET] /api/food 인 getFood()는")
    @Nested
    class Testing_getFood {

        @DisplayName("완전한 정보를 입력했을 때 성공한다.")
        @WithMockCustomUser
        @Test
        void getFood_success() throws Exception {

            // given
            given(foodService.getFood(any(FoodRequestDto.class)))
                  .willReturn(List.of(
                        new FoodInfoResponse(new Food(1L, "사과", 100, 100, 20, 1, 1, "전국")),
                        new FoodInfoResponse(new Food(2L, "사과잼", 50, 200, 22, 3, 1, "전국")),
                        new FoodInfoResponse(new Food(3L, "사과파이", 100, 300, 25, 4, 1, "전국"))
                  ));

            // when then
            mockMvc.perform(get(COMMON_API_ADDRESS)
                        .with(csrf().asHeader())
                        .queryParam("name", "사과")
                        .queryParam("lastId", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                  )
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("food-getFood",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                              parameterWithName("name").description("음식명"),
                              parameterWithName("lastId").description("스크롤 가장 마지막 음식 ID")
                        ),
                        responseFields(
                              fieldWithPath("[].foodId").type(double.class).description("음식 ID"),
                              fieldWithPath("[].foodName").type(String.class).description("음식 이름"),
                              fieldWithPath("[].oneServing").type(double.class).description("1회 제공량"),
                              fieldWithPath("[].kcal").type(double.class).description("칼로리"),
                              fieldWithPath("[].carbs").type(double.class).description("탄수화물"),
                              fieldWithPath("[].protein").type(double.class).description("단백질"),
                              fieldWithPath("[].fat").type(double.class).description("지방"),
                              fieldWithPath("[].madeBy").type(String.class).description("제조사")
                        )));
        }
    }

    @DisplayName("[POST] /api/food 인 saveFood()는")
    @Nested
    class Testing_saveFood {

        @WithMockCustomUser
        @DisplayName("완전한 정보를 입력했을 때 성공한다.")
        @Test
        void saveBodyInfo_success() throws Exception {

            // given
            FoodSaveRequest request = new FoodSaveRequest("사과", 100,
                  100, 20, 1, 1, "전국");

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
                  .andDo(document("food-saveBodyInfo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰.")),
                              headerWithName("refresh_token").description("리프레시 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰."))
                        ), requestFields(
                              fieldWithPath("foodName").type(double.class).description("입력 몸무게"),
                              fieldWithPath("oneServing").type(double.class).description("1회 제공량"),
                              fieldWithPath("kcal").type(double.class).description("1회 제공 칼로리"),
                              fieldWithPath("carbs").type(double.class).description("1회 제공 탄수화물"),
                              fieldWithPath("protein").type(double.class).description("1회 제공 단백질"),
                              fieldWithPath("fat").type(double.class).description("1회 제공 지방"),
                              fieldWithPath("madeBy").type(String.class).description("제조사")
                        )));
        }
    }
}
