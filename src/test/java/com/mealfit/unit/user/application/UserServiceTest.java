package com.mealfit.unit.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mealfit.bodyInfo.repository.BodyInfoRepository;
import com.mealfit.common.factory.UserFactory;
import com.mealfit.common.storageService.StorageService;
import com.mealfit.exception.user.DuplicatedUserException;
import com.mealfit.exception.user.UserNotFoundException;
import com.mealfit.exception.user.PasswordCheckException;
import com.mealfit.user.application.EmailService;
import com.mealfit.user.application.UserService;
import com.mealfit.user.application.dto.UserServiceDtoFactory;
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
import com.mealfit.user.domain.EmailEvent;
import com.mealfit.user.domain.ProviderType;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserRepository;
import com.mealfit.user.domain.UserStatus;
import com.mealfit.user.presentation.dto.request.UserSignUpRequest;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
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

@DisplayName("UserServiceTest - 회원 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final String DUPLICATION_USERNAME = "이미 존재하는 아이디입니다.";
    private static final String DUPLICATION_NICKNAME = "이미 존재하는 닉네임입니다.";
    private static final String DUPLICATION_EMAIL = "이미 존재하는 이메일입니다.";
    private static final String PASSWORD_WRONG_PATTERN = "비밀번호는 최소 8자리에 숫자, 영어 문자를 각각 1개 이상 포함해야 합니다.";
    private static final String PASSWORD_NOT_MATCHED_MESSAGE = "비밀번호와 비밀번호 재확인이 일치하지 않습니다.";

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailService emailService;
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


    @DisplayName("signup() 메서드는")
    @Nested
    class Testing_signUp {

        @DisplayName("아이디가")
        @Nested
        class Context_Username_Validation {

            @DisplayName("중복되면 오류가 발생한다.")
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

            @DisplayName("중복되지 않으면 통과한다.")
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

        @DisplayName("닉네임이")
        @Nested
        class Context_Nickname_Validation {

            @DisplayName("중복되면 오류가 발생한다.")
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

            @DisplayName("중복되지 않으면 통과한다.")
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

        @DisplayName("이메일이")
        @Nested
        class Context_Email_Validation {

            @DisplayName("중복되면 오류가 발생한다.")
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

            @DisplayName("중복되지 않으면 통과한다.")
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

        @DisplayName("비밀번호가")
        @Nested
        class Context_Password_Validation {

            @DisplayName("8자리 이하면 오류가 발생한다.")
            @Test
            void password_too_short_fail() {
                String tooShort = "qwe123";

                assertThatThrownBy(() -> userService.validatePassword(tooShort, tooShort))
                      .isInstanceOf(PasswordCheckException.class)
                      .hasMessage(PASSWORD_WRONG_PATTERN);
            }

            @DisplayName("영어 숫자를 최소한 1개 이상 포함하지 않으면 오류가 발생한다.")
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

            @DisplayName("비밀번호 재입력과")
            @Nested
            class Context_PasswordCheck_Validation {

                @DisplayName("일치하지 않으면 오류가 발생한다.")
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

        @DisplayName("모두 일치하면 성공한다. - 일반회원")
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

    @DisplayName("findUserInfo() 메서드는")
    @Nested
    class Testing_findUserProfile {

        @DisplayName("로그인이 되었을 때")
        @Nested
        class Context_Login {

            @DisplayName("자신의 프로필을 조회할 수 있다.")
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

        @DisplayName("로그인이 되어있지 않을 때")
        @Nested
        class Context_Not_Login {

            @DisplayName("자신의 프로필을 조회할 수 없다.")
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

    @DisplayName("changeUserInfo() 메서드는")
    @Nested
    class Testing_changeUserInfo {

        @DisplayName("로그인이 되었을 때")
        @Nested
        class Context_Login {

            @DisplayName("일반 회원은")
            @Nested
            class Context_Normal_User {

                @DisplayName("자신의 프로필을 수정할 수 있다.")
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
                    // 나중에 일치시킬 수 있다면 usingRecursiveComparison() 사용도 나쁘지 않다.
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

            @DisplayName("소셜 회원은")
            @Nested
            class Context_Social_User {

                @DisplayName("소셜회원이 최초 로그인한 경우에는 Normal 상태로 전환된다.")
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
                    UserInfoResponseDto responseDto = userService.fillSocialUserInfo(requestDto);

                    // then
                    // 나중에 일치시킬 수 있다면 usingRecursiveComparison() 사용도 나쁘지 않다.
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


        @DisplayName("로그인이 되어있지 않을 때")
        @Nested
        class Context_Not_Login {

            @DisplayName("자신의 프로필을 수정할 수 없다.")
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

    @DisplayName("changePassword() 메서드는")
    @Nested
    class Testing_changePassword {

        @DisplayName("로그인이 되어있는 경우")
        @Nested
        class Context_Login {

            @DisplayName("이전 비밀번호가 일치하지 않으면 실패한다.")
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

            @DisplayName("변경할 비밀번호와 비밀번호 재확인이 일치하지 않으면 실패한다.")
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

            @DisplayName("모두 일치하면 변경이 가능하다.")
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
                given(passwordEncoder.encode(anyString())).willReturn(password);

                // then
                UserInfoResponseDto userInfoResponseDto = userService.changePassword(dto);
                assertThat(userInfoResponseDto.getPassword()).isEqualTo(password);

                verify(userRepository, times(1))
                      .findByUsername(anyString());
                verify(passwordEncoder, times(1))
                      .encode(anyString());
            }
        }

        @DisplayName("로그인이 되어있지 않은 경우")
        @Nested
        class Context_NotLogin {

            @DisplayName("비밀번호 변경이 불가능하다.")
            @Test
            void changePassword_fail() {

            }
        }
    }

    @DisplayName("changeNutrition() 메서드는")
    @Nested
    class Testing_changeNutrition {

        @DisplayName("로그인 시")
        @Nested
        class Context_Login {

            @DisplayName("Nutrition 정보를 입력하면 수정이 완료됩니다.")
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

        @DisplayName("비로그인 시")
        @Nested
        class Context_Not_Login {

            @DisplayName("수정이 되지 않습니다.")
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

    @DisplayName("showUserInfoList() 메서드는")
    @Nested
    class Testing_showUserProfileList {

        @DisplayName("관리자 계정일 경우 가능합니다")
        @Test
        void showUserInfoList_admin_success() {
            userService.showUserInfoList();
        }

        @DisplayName("관리자 계정이 아닌경우 불가능합니다.")
        @Test
        void showUserInfoList_not_admin_fail() {

        }

    }

    @DisplayName("findUsername() 메서드는")
    @Nested
    class Testing_findUsername {

        @DisplayName("이메일이 일치하는 경우")
        @Nested
        class Context_Matched_Email {

            @DisplayName("이메일 전송에 성공한다.")
            @Test
            void findUsername_matched_email_success() {
                // given
                String correctEmail = "test@gmail.com";

                FindUsernameRequestDto requestDto = UserServiceDtoFactory.findUsernameRequestDto(
                      "redirect_url", correctEmail);

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

        @DisplayName("이메일이 일치하지 않는 경우")
        @Nested
        class Context_NotMatched_Email {

            @DisplayName("이메일 전송에 실패한다.")
            @Test
            void findUsername_notMatched_email_success() {
                // given
                String inCorrectEmail = "test1@gmail.com";

                FindUsernameRequestDto requestDto = UserServiceDtoFactory
                      .findUsernameRequestDto("redirect_url", inCorrectEmail);

                // when then
                assertThatThrownBy(() -> userService.findUsername(requestDto))
                      .isInstanceOf(IllegalArgumentException.class)
                      .hasMessage("잘못된 이메일입니다.");
            }
        }
    }

    @DisplayName("findPassword() 메서드는")
    @Nested
    class Testing_findPassword {

        @DisplayName("이메일이 일치하는 경우")
        @Nested
        class Context_Matched_Email {

            @DisplayName("이메일 전송에 성공한다.")
            @Test
            void findPassword_matched_email_success() {
                // given
                String username = "username";
                String correctEmail = "test@gmail.com";

                given(userRepository.findByUsername(username)).willReturn(
                      Optional.ofNullable(testUser));

                // when
                FindPasswordRequestDto requestDto = UserServiceDtoFactory
                      .findPasswordRequestDto(username, "redirect_url", correctEmail);

                userService.findPassword(requestDto);

                verify(emailService, times(1))
                      .sendEmail(any(EmailEvent.class));
            }
        }

        @DisplayName("이메일이 불일치하는 경우")
        @Nested
        class Context_NotMatched_Email {

            @DisplayName("이메일 전송에 실패한다.")
            @Test
            void findUsername_notMatched_email_success() {
                // given
                String username = "username";
                String inCorrectEmail = "test1@gmail.com";

                FindPasswordRequestDto requestDto = UserServiceDtoFactory
                      .findPasswordRequestDto(username, "redirect_url", inCorrectEmail);

                SendEmailRequestDto sendEmailRequestDto = UserServiceDtoFactory.sendEmailRequestDto(
                      testUser.getLoginInfo().getUsername(),
                      "redirect_url",
                      inCorrectEmail,
                      EmailType.FIND_PASSWORD);

                given(userRepository.findByUsername(username)).willReturn(
                      Optional.ofNullable(testUser));

                // when then
                assertThatThrownBy(() -> userService.findPassword(requestDto))
                      .isInstanceOf(IllegalArgumentException.class)
                      .hasMessage("잘못된 이메일입니다.");
            }
        }
    }
}
