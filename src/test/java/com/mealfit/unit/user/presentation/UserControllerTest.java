package com.mealfit.unit.user.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mealfit.common.factory.UserFactory;
import com.mealfit.config.security.WithMockCustomUser;
import com.mealfit.unit.ControllerTest;
import com.mealfit.user.application.dto.request.ChangeFastingTimeRequestDto;
import com.mealfit.user.application.dto.request.ChangeNutritionRequestDto;
import com.mealfit.user.application.dto.request.ChangeUserInfoRequestDto;
import com.mealfit.user.application.dto.request.ChangeUserPasswordRequestDto;
import com.mealfit.user.application.dto.response.UserInfoResponseDto;
import com.mealfit.user.domain.ProviderType;
import com.mealfit.user.domain.UserStatus;
import com.mealfit.user.presentation.dto.request.ChangeFastingTimeRequest;
import com.mealfit.user.presentation.dto.request.ChangeNutritionRequest;
import com.mealfit.user.presentation.dto.request.ChangeUserPasswordRequest;
import com.mealfit.user.presentation.dto.request.PasswordFindRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

class UserControllerTest extends ControllerTest {

    private static final String COMMON_API_ADDRESS = "/api/user";

    UserInfoResponseDto userInfoResponseDto = UserFactory.mockUserInfoResponseDto(
          1L, "username",
          "nickname", "https://github.com/profileImg", "test@gmail.com",
          80.0, LocalTime.of(11, 0), LocalTime.of(12, 0),
          2000, 200, 150, 50,
          UserStatus.NORMAL, ProviderType.LOCAL);

    @DisplayName("????????? ?????? ?????? ??? ")
    @Nested
    class Context_when_Login {

        @DisplayName(value = "[POST] /api/user/info ?????? ??? ?????? ????????? ????????? ??? ??????.")
        @WithMockCustomUser
        @Test
        void changeUserInfo_success() throws Exception {

            // given
            MockMultipartFile image = new MockMultipartFile("profileImage", "profileTest.jpeg",
                  "image/jpeg", "<<image-data>>".getBytes(StandardCharsets.UTF_8));

            given(userService.changeUserInfo(any())).willReturn(userInfoResponseDto);

            mockMvc.perform(multipart(COMMON_API_ADDRESS + "/info")
                        .file(image)
                        .param("nickname", "new_Nickname")
                        .param("currentWeight", "80")
                        .param("goalWeight", "70")
                        .param("startFasting", "15:00")
                        .param("endFasting", "17:00")
                        .param("kcal", "2000")
                        .param("carbs", "250")
                        .param("protein", "150")
                        .param("fat", "50")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(csrf().asHeader())
                  )
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("user-changeInfo",
                        preprocessRequest(prettyPrint()),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????.")),
                              headerWithName("refresh_token").description("???????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????."))
                        ),
                        requestParameters(
                              parameterWithName("nickname").optional().description("?????????"),
                              parameterWithName("currentWeight").optional().description("?????? ??????")
                                    .attributes(key("constraints").value("0?????? ?????? ???.")),
                              parameterWithName("goalWeight").optional().description("?????? ??????")
                                    .attributes(key("constraints").value("0?????? ?????? ???")),
                              parameterWithName("startFasting").optional().description("?????? ?????? ??????")
                                    .attributes(key("constraints").value("HH:mm ???????????? ???.")),
                              parameterWithName("endFasting").optional().description("?????? ?????? ??????")
                                    .attributes(key("constraints").value("HH:mm ???????????? ???.")),
                              parameterWithName("kcal").optional().description("?????????")
                                    .attributes(key("constraints").value("0 ??????????????? ???.")),
                              parameterWithName("carbs").optional().description("????????????")
                                    .attributes(key("constraints").value("0 ??????????????? ???.")),
                              parameterWithName("protein").optional().description("?????????")
                                    .attributes(key("constraints").value("0 ??????????????? ???.")),
                              parameterWithName("fat").optional().description("??????").attributes()
                                    .attributes(key("constraints").value("0 ??????????????? ???."))
                        ), requestParts(
                              partWithName("profileImage").optional().description("????????? ??????")
                        )));

            verify(userService, times(1))
                  .changeUserInfo(any());
        }

        @DisplayName(value = "[POST] /user/social/signup ?????? ??? ?????? ????????? ?????? ?????? ????????? ??? ??? ??????.")
        @WithMockCustomUser
        @Test
        void socialSignup_success() throws Exception {

            // given
            MockMultipartFile image = new MockMultipartFile("profileImage", "profileTest.jpeg",
                  "image/jpeg", "<<image-data>>".getBytes(StandardCharsets.UTF_8));

            given(userService.changeUserInfo(any(ChangeUserInfoRequestDto.class)))
                  .willReturn(userInfoResponseDto);

            mockMvc.perform(multipart(COMMON_API_ADDRESS + "/social/signup")
                        .file(image)
                        .param("nickname", "new_Nickname")
                        .param("currentWeight", "80")
                        .param("goalWeight", "70")
                        .param("startFasting", "15:00")
                        .param("endFasting", "17:00")
                        .param("kcal", "2000")
                        .param("carbs", "250")
                        .param("protein", "150")
                        .param("fat", "50")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(csrf().asHeader())
                  )
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("user-socialSignUp",
                        preprocessRequest(prettyPrint()),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????.")),
                              headerWithName("refresh_token").description("???????????? ??????")
                                    .attributes(key("constraints").value("Bearer ??????."))
                        ),
                        requestParameters(
                              parameterWithName("nickname").optional().description("?????????"),
                              parameterWithName("currentWeight").optional().description("?????? ??????")
                                    .attributes(key("constraints").value("0?????? ?????? ???.")),
                              parameterWithName("goalWeight").optional().description("?????? ??????")
                                    .attributes(key("constraints").value("0?????? ?????? ???")),
                              parameterWithName("startFasting").optional().description("?????? ?????? ??????")
                                    .attributes(key("constraints").value("HH:mm ???????????? ???.")),
                              parameterWithName("endFasting").optional().description("?????? ?????? ??????")
                                    .attributes(key("constraints").value("HH:mm ???????????? ???.")),
                              parameterWithName("kcal").optional().description("?????????")
                                    .attributes(key("constraints").value("0 ??????????????? ???.")),
                              parameterWithName("carbs").optional().description("????????????")
                                    .attributes(key("constraints").value("0 ??????????????? ???.")),
                              parameterWithName("protein").optional().description("?????????")
                                    .attributes(key("constraints").value("0 ??????????????? ???.")),
                              parameterWithName("fat").optional().description("??????").attributes()
                                    .attributes(key("constraints").value("0 ??????????????? ???."))
                        ), requestParts(
                              partWithName("profileImage").optional().description("????????? ??????")
                        )));

            verify(userService, times(1))
                  .changeUserInfo(any(ChangeUserInfoRequestDto.class));
        }

        @DisplayName(value = "[GET] /user/userInfo ?????? ??? ?????? ????????? ????????????.")
        @WithMockCustomUser
        @Test
        void userInfo_success() throws Exception {

            //given
            given(userService.showUserInfo(anyString())).willReturn(userInfoResponseDto);

            mockMvc.perform(get(COMMON_API_ADDRESS + "/info")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf().asHeader()))
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("user-getInfo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                              headerWithName("AUTHORIZATION").description("????????? ??????"),
                              headerWithName("refresh_token").description("???????????? ??????")
                        ),
                        responseFields(
                              fieldWithPath("userId").type(String.class)
                                    .description("?????? ?????????"),
                              fieldWithPath("userProfile.nickname").type(String.class)
                                    .description("?????????"),
                              fieldWithPath("userProfile.profileImage").type(String.class)
                                    .description("????????? ??????"),
                              fieldWithPath("userProfile.goalWeight").type(double.class)
                                    .description("?????? ??????"),
                              fieldWithPath("userProfile.userStatus").type(String.class)
                                    .description("?????? ?????? ?????? (?????????, ?????? ?????????, ??????"),
                              fieldWithPath("userProfile.providerType").type(String.class)
                                    .description("????????? ?????? (??????, KAKAO, NAVER, GOOGLE)"),
                              fieldWithPath("fastingInfo.startFasting").type(LocalTime.class)
                                    .description("?????? ?????? ??????"),
                              fieldWithPath("fastingInfo.endFasting").type(LocalTime.class)
                                    .description("?????? ?????? ??????"),
                              fieldWithPath("nutritionGoal.kcal").type(double.class)
                                    .description("?????? ?????? ?????????"),
                              fieldWithPath("nutritionGoal.carbs").type(double.class)
                                    .description("?????? ?????? ????????????"),
                              fieldWithPath("nutritionGoal.protein").type(double.class)
                                    .description("?????? ?????? ?????????"),
                              fieldWithPath("nutritionGoal.fat").type(double.class)
                                    .description("?????? ?????? ??????")
                        )));

            verify(userService, times(1))
                  .showUserInfo(anyString());
        }

        @DisplayName(value = "[PUT] /user/password ?????? ??? ??????????????? ????????????.")
        @WithMockCustomUser
        @Test
        void changePassword_success() throws Exception {

            //given
            ChangeUserPasswordRequest request = new ChangeUserPasswordRequest(
                  "password1", "changePassword1", "changePassword1");

            given(userService.changePassword(any(ChangeUserPasswordRequestDto.class))).willReturn(
                  userInfoResponseDto);

            mockMvc.perform(put(COMMON_API_ADDRESS + "/password")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                        .with(csrf().asHeader()))
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("user-changePassword",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                              fieldWithPath("password").type(String.class)
                                    .description("?????? ????????????"),
                              fieldWithPath("changePassword").type(String.class)
                                    .description("????????? ????????????"),
                              fieldWithPath("passwordCheck").type(String.class)
                                    .description("????????? ???????????? ?????????")
                        ),
                        requestHeaders(
                              headerWithName("AUTHORIZATION").description("????????? ??????"),
                              headerWithName("refresh_token").description("???????????? ??????")
                        ),
                        responseFields(
                              fieldWithPath("userId").type(String.class)
                                    .description("?????? ?????????"),
                              fieldWithPath("userProfile.nickname").type(String.class)
                                    .description("?????????"),
                              fieldWithPath("userProfile.profileImage").type(String.class)
                                    .description("????????? ??????"),
                              fieldWithPath("userProfile.goalWeight").type(double.class)
                                    .description("?????? ??????"),
                              fieldWithPath("userProfile.userStatus").type(String.class)
                                    .description("?????? ?????? ?????? (?????????, ?????? ?????????, ??????"),
                              fieldWithPath("userProfile.providerType").type(String.class)
                                    .description("????????? ?????? (??????, KAKAO, NAVER, GOOGLE)"),
                              fieldWithPath("fastingInfo.startFasting").type(LocalTime.class)
                                    .description("?????? ?????? ??????"),
                              fieldWithPath("fastingInfo.endFasting").type(LocalTime.class)
                                    .description("?????? ?????? ??????"),
                              fieldWithPath("nutritionGoal.kcal").type(double.class)
                                    .description("?????? ?????? ?????????"),
                              fieldWithPath("nutritionGoal.carbs").type(double.class)
                                    .description("?????? ?????? ????????????"),
                              fieldWithPath("nutritionGoal.protein").type(double.class)
                                    .description("?????? ?????? ?????????"),
                              fieldWithPath("nutritionGoal.fat").type(double.class)
                                    .description("?????? ?????? ??????")
                        )));

            verify(userService, times(1))
                  .changePassword(any(ChangeUserPasswordRequestDto.class));
        }

        @DisplayName(value = "[PUT] /user/nutrition ?????? ??? ?????? ?????? ??????????????? ????????????.")
        @WithMockCustomUser
        @Test
        void changeNutrition_success() throws Exception {

            //given
            ChangeNutritionRequest request = new ChangeNutritionRequest(2000, 150,
                  150, 80);

            given(userService.changeNutrition(any(ChangeNutritionRequestDto.class)))
                  .willReturn(userInfoResponseDto);

            mockMvc.perform(put(COMMON_API_ADDRESS + "/nutrition")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                        .with(csrf().asHeader()))
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("user-changeNutrition",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                              fieldWithPath("kcal").type(LocalTime.class).description("?????????"),
                              fieldWithPath("carbs").type(LocalTime.class).description("????????????"),
                              fieldWithPath("protein").type(LocalTime.class).description("?????????"),
                              fieldWithPath("fat").type(LocalTime.class).description("??????")
                        ),
                        requestHeaders(
                              headerWithName("AUTHORIZATION").description("????????? ??????"),
                              headerWithName("refresh_token").description("???????????? ??????")
                        ),
                        responseFields(
                              fieldWithPath("userId").type(String.class)
                                    .description("?????? ?????????"),
                              fieldWithPath("userProfile.nickname").type(String.class)
                                    .description("?????????"),
                              fieldWithPath("userProfile.profileImage").type(String.class)
                                    .description("????????? ??????"),
                              fieldWithPath("userProfile.goalWeight").type(double.class)
                                    .description("?????? ??????"),
                              fieldWithPath("userProfile.userStatus").type(String.class)
                                    .description("?????? ?????? ?????? (?????????, ?????? ?????????, ??????"),
                              fieldWithPath("userProfile.providerType").type(String.class)
                                    .description("????????? ?????? (??????, KAKAO, NAVER, GOOGLE)"),
                              fieldWithPath("fastingInfo.startFasting").type(LocalTime.class)
                                    .description("?????? ?????? ??????"),
                              fieldWithPath("fastingInfo.endFasting").type(LocalTime.class)
                                    .description("?????? ?????? ??????"),
                              fieldWithPath("nutritionGoal.kcal").type(double.class)
                                    .description("?????? ?????? ?????????"),
                              fieldWithPath("nutritionGoal.carbs").type(double.class)
                                    .description("?????? ?????? ????????????"),
                              fieldWithPath("nutritionGoal.protein").type(double.class)
                                    .description("?????? ?????? ?????????"),
                              fieldWithPath("nutritionGoal.fat").type(double.class)
                                    .description("?????? ?????? ??????")
                        )));

            verify(userService, times(1))
                  .changeNutrition(any(ChangeNutritionRequestDto.class));
        }

        @DisplayName(value = "[PUT] /user/fastingTime ?????? ??? ?????? ?????? ??????????????? ????????????.")
        @WithMockCustomUser
        @Test
        void changeFastingTime_success() throws Exception {

            //given
            ChangeFastingTimeRequest request = new ChangeFastingTimeRequest(LocalTime.now(),
                  LocalTime.now());
            given(userService.changeFastingTime(any(ChangeFastingTimeRequestDto.class)))
                  .willReturn(userInfoResponseDto);

            mockMvc.perform(put(COMMON_API_ADDRESS + "/fastingTime")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                        .with(csrf().asHeader()))
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("user-changeFastingTime",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                              fieldWithPath("startFasting").type(LocalTime.class)
                                    .description("?????? ?????? ??????"),
                              fieldWithPath("endFasting").type(LocalTime.class)
                                    .description("?????? ?????? ??????")
                        ),
                        requestHeaders(
                              headerWithName("AUTHORIZATION").description("????????? ??????"),
                              headerWithName("refresh_token").description("???????????? ??????")
                        ),
                        responseFields(
                              fieldWithPath("userId").type(String.class)
                                    .description("?????? ?????????"),
                              fieldWithPath("userProfile.nickname").type(String.class)
                                    .description("?????????"),
                              fieldWithPath("userProfile.profileImage").type(String.class)
                                    .description("????????? ??????"),
                              fieldWithPath("userProfile.goalWeight").type(double.class)
                                    .description("?????? ??????"),
                              fieldWithPath("userProfile.userStatus").type(String.class)
                                    .description("?????? ?????? ?????? (?????????, ?????? ?????????, ??????"),
                              fieldWithPath("userProfile.providerType").type(String.class)
                                    .description("????????? ?????? (??????, KAKAO, NAVER, GOOGLE)"),
                              fieldWithPath("fastingInfo.startFasting").type(LocalTime.class)
                                    .description("?????? ?????? ??????"),
                              fieldWithPath("fastingInfo.endFasting").type(LocalTime.class)
                                    .description("?????? ?????? ??????"),
                              fieldWithPath("nutritionGoal.kcal").type(double.class)
                                    .description("?????? ?????? ?????????"),
                              fieldWithPath("nutritionGoal.carbs").type(double.class)
                                    .description("?????? ?????? ????????????"),
                              fieldWithPath("nutritionGoal.protein").type(double.class)
                                    .description("?????? ?????? ?????????"),
                              fieldWithPath("nutritionGoal.fat").type(double.class)
                                    .description("?????? ?????? ??????")
                        )));

            verify(userService, times(1))
                  .changeFastingTime(any(ChangeFastingTimeRequestDto.class));
        }
    }

    @DisplayName("????????? ?????? ?????? ?????? ??? ")
    @Nested
    class Context_when_not_Login {

        @DisplayName(value = "[POST] /user/signup ?????? ??? ??????????????? ????????????.")
        @WithMockCustomUser
        @Test
        void signup_success() throws Exception {

            MockMultipartFile image = new MockMultipartFile("profileImage",
                  "profileTest.jpeg", "image/jpeg",
                  "<<image data>>".getBytes(StandardCharsets.UTF_8));

            mockMvc.perform(multipart(COMMON_API_ADDRESS + "/signup")
                        .file(image)
                        .param("username", "test123")
                        .param("email", "test@gmail.com")
                        .param("password", "qwe123123")
                        .param("passwordCheck", "qwe123123")
                        .param("nickname", "nickname")
                        .param("currentWeight", "80")
                        .param("goalWeight", "70")
                        .param("startFasting", "15:00")
                        .param("endFasting", "17:00")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(csrf().asHeader()))
                  .andExpect(status().isCreated())
                  .andDo(print())
                  .andDo(document("user-signup",
                        preprocessRequest(prettyPrint()),
                        requestParameters(
                              parameterWithName("username").description("?????????"),
                              parameterWithName("email").description("?????????")
                                    .attributes(key("constraints").value("????????? ??????")),
                              parameterWithName("password").description("????????????")
                                    .attributes(key("constraints").value("?????? ?????? ?????? 8?????? ??????")),
                              parameterWithName("passwordCheck").description("???????????? ??????")
                                    .attributes(key("constraints").value("??????????????? ???????????? ???.")),
                              parameterWithName("nickname").description("?????????"),
                              parameterWithName("currentWeight").description("?????? ?????????")
                                    .attributes(key("constraints").value("0?????? ?????? ???.")),
                              parameterWithName("goalWeight").description("?????? ?????????")
                                    .attributes(key("constraints").value("0?????? ?????? ???.")),
                              parameterWithName("startFasting").description("?????? ?????? ??????")
                                    .attributes(key("constraints").value("HH:mm ???????????? ???.")),
                              parameterWithName("endFasting").description("?????? ?????? ??????")
                                    .attributes(key("constraints").value("HH:mm ???????????? ???."))
                        ), requestParts(
                              partWithName("profileImage").optional().description("????????? ??????")
                        )));
        }

        @DisplayName(value = "[GET] /user/{username}/{?????????} ?????? ??? ????????? ?????? ????????? ????????????.")
        @WithMockCustomUser
        @Test
        void validateUsername_success() throws Exception {
            mockMvc.perform(
                        get(COMMON_API_ADDRESS + "/{key}/{value}", "username", "test123@gmail.com"))
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("user-usernameValidation",
                        preprocessRequest(prettyPrint())));
        }

        @DisplayName(value = "[GET] /user/{email}/{?????????} ?????? ??? ????????? ?????? ????????? ????????????.")
        @WithMockCustomUser
        @Test
        void validateEmail_success() throws Exception {
            mockMvc.perform(
                        get(COMMON_API_ADDRESS + "/{key}/{value}", "email", "test123@gmail.com"))
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("user-emailValidation",
                        preprocessRequest(prettyPrint())));
        }

        @DisplayName(value = "[GET] /user/{nickname}/{?????????} ?????? ??? ????????? ?????? ????????? ????????????.")
        @WithMockCustomUser
        @Test
        void validateNickname_success() throws Exception {
            mockMvc.perform(get(COMMON_API_ADDRESS + "/{key}/{value}", "nickname", "test2"))
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("user-nicknameValidation",
                        preprocessRequest(prettyPrint())));
        }

//        @DisplayName(value = "[POST] /user/find/username ?????? ??? ???????????? ???????????? ????????????.")
//        @WithMockCustomUser
//        @Test
//        void findUsername_success() throws Exception {
//
//            // given
//            TextNode request = new TextNode("test@gmail.com");
//
//            mockMvc.perform(post(COMMON_API_ADDRESS + "/find/username")
//                        .content(objectMapper.writeValueAsBytes(request))
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .with(csrf().asHeader()))
//                  .andExpect(status().isOk())
//                  .andDo(print())
//                  .andDo(document("user-findUsername",
//                        preprocessRequest(prettyPrint()),
//                        requestFields(
//                              fieldWithPath("email").type(TextNode.class).description("?????????")
//                        )));
//        }

        @DisplayName(value = "[POST] /user/find/password ?????? ??? ?????? ??????????????? ???????????? ????????????.")
        @WithMockCustomUser
        @Test
        void findPassword_success() throws Exception {

            PasswordFindRequest username = new PasswordFindRequest("test@gmail.com", "username");

            mockMvc.perform(post(COMMON_API_ADDRESS + "/find/password")
                        .content(objectMapper.writeValueAsBytes(username))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf().asHeader()))
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("user-findPassword",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                              fieldWithPath("email").type(String.class).description("?????????"),
                              fieldWithPath("username").type(String.class).description("?????? ?????????")
                        )
                  ));
        }
    }
}