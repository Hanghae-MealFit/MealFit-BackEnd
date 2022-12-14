package com.mealfit.unit.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mealfit.bodyInfo.domain.BodyInfoRepository;
import com.mealfit.common.factory.UserFactory;
import com.mealfit.common.storageService.StorageService;
import com.mealfit.exception.user.DuplicatedUserException;
import com.mealfit.exception.user.PasswordCheckException;
import com.mealfit.exception.user.UserNotFoundException;
import com.mealfit.user.application.EmailEventHandler;
import com.mealfit.user.application.UserService;
import com.mealfit.user.application.dto.UserServiceDtoFactory;
import com.mealfit.user.application.dto.request.ChangeFastingTimeRequestDto;
import com.mealfit.user.application.dto.request.ChangeNutritionRequestDto;
import com.mealfit.user.application.dto.request.ChangeUserInfoRequestDto;
import com.mealfit.user.application.dto.request.ChangeUserPasswordRequestDto;
import com.mealfit.user.application.dto.request.CheckDuplicateSignupInputDto;
import com.mealfit.user.application.dto.request.FindPasswordRequestDto;
import com.mealfit.user.application.dto.request.FindUsernameRequestDto;
import com.mealfit.user.application.dto.request.SendEmailRequestDto;
import com.mealfit.user.application.dto.request.SendEmailRequestDto.EmailType;
import com.mealfit.user.application.dto.request.UserSignUpRequestDto;
import com.mealfit.user.application.dto.response.UserInfoResponseDto;
import com.mealfit.user.domain.ProviderType;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserRepository;
import com.mealfit.user.domain.UserStatus;
import com.mealfit.user.presentation.dto.request.PasswordFindRequest;
import com.mealfit.user.presentation.dto.request.UserSignUpRequest;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayName("UserServiceTest - ?????? ????????? ?????????")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final String DUPLICATION_USERNAME = "?????? ???????????? ??????????????????.";
    private static final String DUPLICATION_NICKNAME = "?????? ???????????? ??????????????????.";
    private static final String DUPLICATION_EMAIL = "?????? ???????????? ??????????????????.";
    private static final String PASSWORD_WRONG_PATTERN = "??????????????? ?????? 8????????? ??????, ?????? ????????? ?????? 1??? ?????? ???????????? ?????????.";
    private static final String PASSWORD_NOT_MATCHED_MESSAGE = "??????????????? ???????????? ???????????? ???????????? ????????????.";

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailEventHandler emailEventHandler;
    @Mock
    private StorageService storageService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private BodyInfoRepository bodyInfoRepository;
    @Mock
    private ApplicationEventPublisher publisher;

    User testUser = UserFactory.mockUser("username", "password123",
          "nickname1", "test@gmail.com", "https://github.com/testImage.jpg",
          70, LocalTime.of(10, 0), LocalTime.of(12, 0),
          2000, 200, 120, 50,
          UserStatus.NORMAL);


    @DisplayName("signup() ????????????")
    @Nested
    class Testing_signUp {

        @DisplayName("????????????")
        @Nested
        class Context_Username_Validation {

            @DisplayName("???????????? ????????? ????????????.")
            @Test
            void username_duplicate_fail() {

                // given
                String duplicatedUsername = "test";
                CheckDuplicateSignupInputDto dto =
                      UserServiceDtoFactory.checkDuplicateSignupInput("username",
                            duplicatedUsername);

                given(userRepository.existsByUsername(duplicatedUsername)).willReturn(true);

                // when

                // then
                assertThatThrownBy(() -> userService.checkDuplicateSignupInput(dto))
                      .isInstanceOf(DuplicatedUserException.class)
                      .hasMessage(DUPLICATION_USERNAME);
            }

            @DisplayName("???????????? ????????? ????????????.")
            @Test
            void username_NOT_duplicate_success() {

                // given
                String notDuplicatedUsername = "test1";
                CheckDuplicateSignupInputDto dto =
                      UserServiceDtoFactory.checkDuplicateSignupInput("username",
                            notDuplicatedUsername);

                // then
                assertThatNoException().isThrownBy(
                      () -> userService.checkDuplicateSignupInput(dto));
            }
        }

        @DisplayName("????????????")
        @Nested
        class Context_Nickname_Validation {

            @DisplayName("???????????? ????????? ????????????.")
            @Test
            void nickname_duplicate_fail() {

                //given
                String duplicateNickname = "nickname1";
                CheckDuplicateSignupInputDto dto =
                      UserServiceDtoFactory.checkDuplicateSignupInput("nickname",
                            duplicateNickname);

                // when
                given(userRepository.existsByNickname(duplicateNickname)).willReturn(true);

                // then
                assertThatThrownBy(() -> userService.checkDuplicateSignupInput(dto))
                      .isInstanceOf(DuplicatedUserException.class)
                      .hasMessage(DUPLICATION_NICKNAME);
            }

            @DisplayName("???????????? ????????? ????????????.")
            @Test
            void nickname_NOT_duplicate_success() {
                // given
                String notDuplicateNickname = "nickname12";
                CheckDuplicateSignupInputDto dto =
                      UserServiceDtoFactory.checkDuplicateSignupInput("nickname",
                            notDuplicateNickname);

                // then
                assertThatNoException().isThrownBy(
                      () -> userService.checkDuplicateSignupInput(dto));
            }
        }

        @DisplayName("????????????")
        @Nested
        class Context_Email_Validation {

            @DisplayName("???????????? ????????? ????????????.")
            @Test
            void email_duplicate_fail() {
                // given
                String duplicateEmail = "test@gmail.com";
                CheckDuplicateSignupInputDto dto =
                      UserServiceDtoFactory.checkDuplicateSignupInput("email", duplicateEmail);
                // when
                when(userRepository.existsByEmail(duplicateEmail)).thenReturn(true);

                // then
                assertThatThrownBy(() -> userService.checkDuplicateSignupInput(dto))
                      .isInstanceOf(DuplicatedUserException.class)
                      .hasMessage(DUPLICATION_EMAIL);
            }

            @DisplayName("???????????? ????????? ????????????.")
            @Test
            void email_NOT_duplicate_success() {

                // given
                String notDuplicateEmail = "test1@gmail.com";
                CheckDuplicateSignupInputDto dto =
                      UserServiceDtoFactory.checkDuplicateSignupInput("email", notDuplicateEmail);
                System.out.println(dto.getKey());
                System.out.println(dto.getValue());

                // then
                assertThatNoException().isThrownBy(
                      () -> userService.checkDuplicateSignupInput(dto));
            }
        }

        @DisplayName("???????????????")
        @Nested
        class Context_Password_Validation {

            @DisplayName("8?????? ????????? ????????? ????????????.")
            @Test
            void password_too_short_fail() {
                String tooShort = "qwe123";

                assertThatThrownBy(() -> userService.validatePassword(tooShort, tooShort))
                      .isInstanceOf(PasswordCheckException.class)
                      .hasMessage(PASSWORD_WRONG_PATTERN);
            }

            @DisplayName("?????? ????????? ????????? 1??? ?????? ???????????? ????????? ????????? ????????????.")
            @Test
            void password_Not_include_number_english_fail() {
                String onlyEnglish = "qweqweeqw";
                String onlyNumber = "12312312321";

                assertThatThrownBy(
                      () -> userService.validatePassword(onlyEnglish, onlyEnglish))
                      .isInstanceOf(PasswordCheckException.class)
                      .hasMessage(PASSWORD_WRONG_PATTERN);

                assertThatThrownBy(() -> userService.validatePassword(onlyNumber, onlyNumber))
                      .isInstanceOf(PasswordCheckException.class)
                      .hasMessage(PASSWORD_WRONG_PATTERN);
            }

            @DisplayName("???????????? ????????????")
            @Nested
            class Context_PasswordCheck_Validation {

                @DisplayName("???????????? ????????? ????????? ????????????.")
                @Test
                void NOT_same_with_passwordCheck_fail() {
                    String password1 = "qwe11111";
                    String notMatchedPassword = "qwe22222";

                    assertThatThrownBy(
                          () -> userService.validatePassword(password1, notMatchedPassword))
                          .isInstanceOf(PasswordCheckException.class)
                          .hasMessage(PASSWORD_NOT_MATCHED_MESSAGE);
                }

                @Test
                void same_with_passwordCheck_Success() {
                    String password1 = "qwe11111";
                    String MatchedPassword = "qwe11111";

                    assertThatNoException().isThrownBy(() ->
                          userService.validatePassword(password1, MatchedPassword));
                }
            }
        }

        @DisplayName("?????? ???????????? ????????????. - ????????????")
        @Test
        void all_pass_success() {

            String imageUrl = "http://github.testImage.jpg";
            LocalTime start = LocalTime.of(10, 0);
            LocalTime end = LocalTime.of(12, 0);

            // given
            UserSignUpRequest request = UserSignUpRequest.builder()
                  .username("username")
                  .email("test@gmail.com")
                  .password("qwe123123")
                  .passwordCheck("qwe123123")
                  .nickname("nickname1")
                  .profileImage(new MockMultipartFile("testImage", "hello".getBytes()))
                  .currentWeight(90.0)
                  .goalWeight(70.0)
                  .startFasting(start)
                  .endFasting(end)
                  .build();

            UserSignUpRequestDto dto = UserServiceDtoFactory
                  .userSignUpRequestDto("TEST_DOMAIN_URL", request);

            // when
            given(userRepository.save(any(User.class))).willReturn(testUser);
            given(storageService.uploadMultipartFile(anyList(), anyString()))
                  .willReturn(List.of(imageUrl));

            UserInfoResponseDto responseDto = userService.signup(dto);

            // then
            assertEquals(request.getUsername(), responseDto.getUsername());
            assertEquals(request.getEmail(), responseDto.getEmail());
            assertEquals(request.getNickname(), responseDto.getNickname());
            assertEquals(request.getGoalWeight(), responseDto.getGoalWeight());
            assertEquals(request.getStartFasting(), responseDto.getStartFasting());
            assertEquals(request.getEndFasting(), responseDto.getEndFasting());
            assertEquals(ProviderType.LOCAL, responseDto.getProviderType());
            assertEquals(2000, responseDto.getKcal());
            assertEquals(200, responseDto.getCarbs());
            assertEquals(120, responseDto.getProtein());
            assertEquals(50, responseDto.getFat());
        }

    }

    @DisplayName("findUserInfo() ????????????")
    @Nested
    class Testing_findUserProfile {

        @DisplayName("???????????? ????????? ???")
        @Nested
        class Context_Login {

            @DisplayName("????????? ???????????? ????????? ??? ??????.")
            @Test
            void getUserInfo_WithMyUsername_Success() {
                String loginUsername = "test1";

                // given
                given(userRepository.findByUsername(loginUsername)).willReturn(
                      Optional.ofNullable(testUser));

                // when
                UserInfoResponseDto userResponseDto = userService
                      .showUserInfo("test1");

                // then
                assertThat(userResponseDto).isNotNull();
                verify(userRepository, times(1))
                      .findByUsername("test1");
            }
        }

        @DisplayName("???????????? ???????????? ?????? ???")
        @Nested
        class Context_Not_Login {

            @DisplayName("????????? ???????????? ????????? ??? ??????.")
            @Test
            void getUserInfo_WithMyUsername_fail() {
                String notLoginUsername = null;

                // when then
                assertThatThrownBy(() -> userService.showUserInfo(notLoginUsername))
                      .isInstanceOf(UserNotFoundException.class);

                verify(userRepository, times(1))
                      .findByUsername(null);
            }
        }
    }

    @DisplayName("changeUserInfo() ????????????")
    @Nested
    class Testing_changeUserInfo {

        @DisplayName("???????????? ????????? ???")
        @Nested
        class Context_Login {

            @DisplayName("?????? ?????????")
            @Nested
            class Context_Normal_User {

                @DisplayName("????????? ???????????? ????????? ??? ??????.")
                @Test
                void changeUserInfo_WithMyUsername_Success() {
                    String loginUsername = "username";

                    ChangeUserInfoRequestDto requestDto = UserFactory.mockChangeUserInfoRequestDto(
                          loginUsername,
                          "newNickname", null,
                          80, 75,
                          LocalTime.of(9, 10), LocalTime.of(11, 10),
                          2500, 250, 150, 50);

                    // given
                    given(userRepository.findByUsername(loginUsername)).willReturn(
                          Optional.ofNullable(testUser));

                    // when
                    UserInfoResponseDto responseDto = userService.changeUserInfo(requestDto);

                    // then
                    // ????????? ???????????? ??? ????????? usingRecursiveComparison() ????????? ????????? ??????.
                    assertThat(requestDto.getNickname()).isEqualTo(responseDto.getNickname());
                    assertThat(requestDto.getGoalWeight()).isEqualTo(responseDto.getGoalWeight());
                    assertThat(requestDto.getStartFasting()).isEqualTo(
                          responseDto.getStartFasting());
                    assertThat(requestDto.getEndFasting()).isEqualTo(responseDto.getEndFasting());
                    assertThat(requestDto.getKcal()).isEqualTo(responseDto.getKcal());
                    assertThat(requestDto.getCarbs()).isEqualTo(responseDto.getCarbs());
                    assertThat(requestDto.getProtein()).isEqualTo(responseDto.getProtein());
                    assertThat(requestDto.getFat()).isEqualTo(responseDto.getFat());

                    verify(userRepository, times(1))
                          .findByUsername(loginUsername);
                }
            }

            @DisplayName("?????? ?????????")
            @Nested
            class Context_Social_User {

                @DisplayName("??????????????? ?????? ???????????? ???????????? Normal ????????? ????????????.")
                @Test
                void changeUserInfo_WithSocialUser_Success() {

                    // given
                    String loginUsername = "socialUser";

                    User socialUser = UserFactory.mockSocialUser(loginUsername,
                          null, null,
                          ProviderType.KAKAO, UserStatus.FIRST_LOGIN);

                    ChangeUserInfoRequestDto requestDto = UserFactory.mockChangeUserInfoRequestDto(
                          loginUsername,
                          "new_social_nickname",
                          new MockMultipartFile("profileImg", "Content".getBytes()),
                          80, 75,
                          LocalTime.of(9, 10), LocalTime.of(11, 10),
                          2500, 250, 150, 50);

                    given(userRepository.findByUsername(loginUsername)).willReturn(
                          Optional.ofNullable(socialUser));
                    given(storageService.uploadMultipartFile(anyList(), anyString())).willReturn(
                          List.of("https://github.com/testImage.jpg"));

                    // when
                    UserInfoResponseDto responseDto = userService.changeUserInfo(requestDto);

                    // then
                    // ????????? ???????????? ??? ????????? usingRecursiveComparison() ????????? ????????? ??????.
                    assertThat(requestDto.getNickname()).isEqualTo(responseDto.getNickname());
                    assertThat(requestDto.getGoalWeight()).isEqualTo(responseDto.getGoalWeight());
                    assertThat(requestDto.getStartFasting()).isEqualTo(
                          responseDto.getStartFasting());
                    assertThat(requestDto.getEndFasting()).isEqualTo(responseDto.getEndFasting());
                    assertThat(requestDto.getKcal()).isEqualTo(responseDto.getKcal());
                    assertThat(requestDto.getCarbs()).isEqualTo(responseDto.getCarbs());
                    assertThat(requestDto.getProtein()).isEqualTo(responseDto.getProtein());
                    assertThat(requestDto.getFat()).isEqualTo(responseDto.getFat());
                    assertThat(responseDto.getProfileImage()).isEqualTo(
                          "https://github.com/testImage.jpg");
                    assertThat(responseDto.getUserStatus()).isEqualTo(UserStatus.NORMAL);

                    verify(userRepository, times(1))
                          .findByUsername(loginUsername);
                    verify(storageService, times(1))
                          .uploadMultipartFile(anyList(), anyString());
                }
            }
        }


        @DisplayName("???????????? ???????????? ?????? ???")
        @Nested
        class Context_Not_Login {

            @DisplayName("????????? ???????????? ????????? ??? ??????.")
            @Test
            void getUserInfo_WithMyUsername_fail() {
                String notLoginUsername = null;

                ChangeUserInfoRequestDto dto = UserFactory.mockChangeUserInfoRequestDto(
                      notLoginUsername, "newNickname", null, 0,
                      0, null, null,
                      0, 0, 0, 0);

                // given
                given(userRepository.findByUsername(notLoginUsername)).willReturn(
                      Optional.empty());

                // when then
                assertThatThrownBy(() -> userService.changeUserInfo(dto))
                      .isInstanceOf(UserNotFoundException.class);

                verify(userRepository, times(1))
                      .findByUsername(null);
            }
        }
    }

    @DisplayName("changePassword() ????????????")
    @Nested
    class Testing_changePassword {

        @DisplayName("???????????? ???????????? ??????")
        @Nested
        class Context_Login {

            @DisplayName("?????? ??????????????? ???????????? ????????? ????????????.")
            @Test
            void changePassword_not_match_originPassword_fail() {
                // given
                String password = "password12";
                String changePassword = "password1";
                String checkPassword = "password1";

                ChangeUserPasswordRequestDto dto = UserFactory.mockChangeUserPasswordRequestDto(
                      "username", password, changePassword, checkPassword);

                // when
                given(userRepository.findByUsername(anyString())).willReturn(
                      Optional.ofNullable(testUser));

                // then
                assertThatThrownBy(() -> userService.changePassword(dto))
                      .isInstanceOf(BadCredentialsException.class);

                verify(userRepository, times(1))
                      .findByUsername(anyString());
            }

            @DisplayName("????????? ??????????????? ???????????? ???????????? ???????????? ????????? ????????????.")
            @Test
            void changePassword_unMatch_password_fail() {
                // given
                String password = "password123";
                String changePassword = "password1";
                String checkPassword = "password2";

                ChangeUserPasswordRequestDto dto = UserFactory.mockChangeUserPasswordRequestDto(
                      "username", password, changePassword, checkPassword);

                // when then
                assertThatThrownBy(() -> userService.changePassword(dto))
                      .isInstanceOf(PasswordCheckException.class);
            }

            @DisplayName("?????? ???????????? ????????? ????????????.")
            @Test
            void changePassword_match_password_Success() {
                // given
                String password = "password123";
                String changePassword = "password1";
                String checkPassword = "password1";

                ChangeUserPasswordRequestDto dto = UserFactory.mockChangeUserPasswordRequestDto(
                      "username", password, changePassword, checkPassword);

                // when
                given(userRepository.findByUsername(anyString())).willReturn(
                      Optional.ofNullable(testUser));
                given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

                // then
                UserInfoResponseDto userInfoResponseDto = userService.changePassword(dto);

                verify(userRepository, times(1))
                      .findByUsername(anyString());
                verify(passwordEncoder, times(1))
                      .matches(anyString(), anyString());
            }
        }

        @DisplayName("???????????? ???????????? ?????? ??????")
        @Nested
        class Context_NotLogin {

            @DisplayName("???????????? ????????? ???????????????.")
            @Test
            void changePassword_fail() {

            }
        }
    }

    @DisplayName("changeNutrition() ????????????")
    @Nested
    class Testing_changeNutrition {

        @DisplayName("????????? ???")
        @Nested
        class Context_Login {

            @DisplayName("Nutrition ????????? ???????????? ????????? ???????????????.")
            @Test
            void changeNutrition_success() {

                String loginUsername = "username";

                ChangeNutritionRequestDto requestDto = UserFactory.mockNutritionRequestDto(
                      loginUsername, 2500, 300, 150, 50);

                given(userRepository.findByUsername(requestDto.getUsername())).willReturn(
                      Optional.ofNullable(testUser));

                UserInfoResponseDto responseDto = userService.changeNutrition(requestDto);

                assertEquals(responseDto.getKcal(), requestDto.getKcal());
                assertEquals(responseDto.getCarbs(), requestDto.getCarbs());
                assertEquals(responseDto.getProtein(), requestDto.getProtein());
                assertEquals(responseDto.getFat(), requestDto.getFat());
            }
        }

        @DisplayName("???????????? ???")
        @Nested
        class Context_Not_Login {

            @DisplayName("????????? ?????? ????????????.")
            @Test
            void changeNutrition_fail() {
                String notLoginUsername = null;

                ChangeNutritionRequestDto requestDto = UserFactory.mockNutritionRequestDto(
                      notLoginUsername, 2500, 300, 150, 50);

                assertThatThrownBy(() -> userService.changeNutrition(requestDto))
                      .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

    @DisplayName("showUserInfoList() ????????????")
    @Nested
    class Testing_showUserProfileList {

        @DisplayName("????????? ????????? ?????? ???????????????")
        @Test
        void showUserInfoList_admin_success() {
            userService.showUserInfoList();
        }

        @DisplayName("????????? ????????? ???????????? ??????????????????.")
        @Test
        void showUserInfoList_not_admin_fail() {

        }

    }

    @DisplayName("findUsername() ????????????")
    @Nested
    class Testing_findUsername {

        @DisplayName("???????????? ???????????? ??????")
        @Nested
        class Context_Matched_Email {

            @DisplayName("????????? ????????? ????????????.")
            @Test
            void findUsername_matched_email_success() {
                // given
                String correctEmail = "test@gmail.com";

                FindUsernameRequestDto requestDto = UserServiceDtoFactory.findUsernameRequestDto(
                      correctEmail);

                SendEmailRequestDto dto = UserServiceDtoFactory.sendEmailRequestDto("username",
                      "redirect_url", correctEmail, EmailType.FIND_USERNAME);

                // when
                given(userRepository.findByEmail(correctEmail)).willReturn(
                      Optional.ofNullable(testUser));

                userService.findUsername(requestDto);

                // then
                verify(userRepository, times(1)).findByEmail(correctEmail);
            }
        }

        @DisplayName("???????????? ???????????? ?????? ??????")
        @Nested
        class Context_NotMatched_Email {

            @DisplayName("????????? ????????? ????????????.")
            @Test
            void findUsername_notMatched_email_success() {
                // given
                String inCorrectEmail = "test1@gmail.com";

                FindUsernameRequestDto requestDto = UserServiceDtoFactory
                      .findUsernameRequestDto(inCorrectEmail);

                // when then
                assertThatThrownBy(() -> userService.findUsername(requestDto))
                      .isInstanceOf(IllegalArgumentException.class)
                      .hasMessage("????????? ??????????????????.");
            }
        }
    }

    @DisplayName("findPassword() ????????????")
    @Nested
    class Testing_findPassword {

        @DisplayName("???????????? ???????????? ??????")
        @Nested
        class Context_Matched_Email {

            @DisplayName("????????? ????????? ????????????.")
            @Test
            void findPassword_matched_email_success() {
                // given
                String username = "username";
                String correctEmail = "test@gmail.com";

                given(userRepository.findByUsername(anyString())).willReturn(
                      Optional.ofNullable(testUser));

                // when
                FindPasswordRequestDto requestDto = UserServiceDtoFactory
                      .findPasswordRequestDto(new PasswordFindRequest(correctEmail, username));

                userService.findPassword(requestDto);

                verify(userRepository, times(1)).findByUsername(anyString());
            }
        }

        @DisplayName("???????????? ??????????????? ??????")
        @Nested
        class Context_NotMatched_Email {

            @DisplayName("????????? ????????? ????????????.")
            @Test
            void findUsername_notMatched_email_success() {
                // given
                String username = "username";
                String inCorrectEmail = "test1@gmail.com";

                FindPasswordRequestDto requestDto = UserServiceDtoFactory
                      .findPasswordRequestDto(new PasswordFindRequest(inCorrectEmail, username));

                given(userRepository.findByUsername(anyString())).willReturn(
                      Optional.ofNullable(testUser));

                // when then
                assertThatThrownBy(() -> userService.findPassword(requestDto))
                      .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @DisplayName("changeFastingTime() ????????????")
    @Nested
    class Testing_changeFastingTime {

        @DisplayName("FastingTime ????????? ???????????? ????????? ???????????????.")
        @Test
        void changeFastingTime_success() {

            ChangeFastingTimeRequestDto requestDto = new ChangeFastingTimeRequestDto(
                  1L, LocalTime.of(10, 0), LocalTime.of(15, 0));

            given(userRepository.findById(anyLong()))
                  .willReturn(Optional.ofNullable(testUser));

            UserInfoResponseDto responseDto = userService.changeFastingTime(requestDto);

            assertEquals(responseDto.getStartFasting(), requestDto.getStartFasting());
            assertEquals(responseDto.getEndFasting(), requestDto.getEndFasting());
        }

        @DisplayName("?????? ???????????? UserNotFoundException??? ????????????..")
        @Test
        void no_user_fail() {

            ChangeFastingTimeRequestDto requestDto = new ChangeFastingTimeRequestDto(
                  1L, LocalTime.of(10, 0), LocalTime.of(15, 0));

            given(userRepository.findById(anyLong()))
                  .willReturn(Optional.empty());

            Assertions.assertThatThrownBy(() -> userService.changeFastingTime(requestDto))
                  .isInstanceOf(UserNotFoundException.class);
        }
    }

    @DisplayName("checkDuplicateSignupInput() ????????????")
    @Nested
    class Testing_checkDuplicateSignupInput {
        @DisplayName("key??? username, email, nickname??? ???????????? ????????? IllegalArgumentException??? ????????????.")
        @Test
        void no_key_value_fail(){
            CheckDuplicateSignupInputDto dto = new CheckDuplicateSignupInputDto(
                  "not_valid_key", "test");

            Assertions.assertThatThrownBy(() -> userService.checkDuplicateSignupInput(dto))
                  .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
