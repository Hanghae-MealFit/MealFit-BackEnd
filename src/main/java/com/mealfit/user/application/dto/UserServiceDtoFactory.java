package com.mealfit.user.application.dto;

import com.mealfit.user.application.dto.request.ChangeFastingTimeRequestDto;
import com.mealfit.user.application.dto.request.CheckDuplicateSignupInputDto;
import com.mealfit.user.application.dto.request.FindPasswordRequestDto;
import com.mealfit.user.application.dto.request.FindUsernameRequestDto;
import com.mealfit.user.application.dto.request.ChangeNutritionRequestDto;
import com.mealfit.user.application.dto.request.ChangeUserInfoRequestDto;
import com.mealfit.user.application.dto.request.ChangeUserPasswordRequestDto;
import com.mealfit.user.application.dto.request.EmailAuthRequestDto;
import com.mealfit.user.application.dto.request.SendEmailRequestDto;
import com.mealfit.user.application.dto.request.SendEmailRequestDto.EmailType;
import com.mealfit.user.application.dto.request.UserSignUpRequestDto;
import com.mealfit.user.application.dto.response.UserInfoResponseDto;
import com.mealfit.user.application.dto.response.UserNutritionGoalResponseDto;
import com.mealfit.user.domain.User;
import com.mealfit.user.presentation.dto.request.ChangeFastingTimeRequest;
import com.mealfit.user.presentation.dto.request.ChangeNutritionRequest;
import com.mealfit.user.presentation.dto.request.ChangeUserInfoRequest;
import com.mealfit.user.presentation.dto.request.ChangeUserPasswordRequest;
import com.mealfit.user.presentation.dto.request.UserSignUpRequest;

// 실제로 Assembler 라고 함.
public class UserServiceDtoFactory {

    public static UserSignUpRequestDto userSignUpRequestDto(String redirectUrl,
          UserSignUpRequest dto) {
        return UserSignUpRequestDto.builder()
              .redirectURL(redirectUrl)
              .username(dto.getUsername())
              .password(dto.getPassword())
              .passwordCheck(dto.getPasswordCheck())
              .email(dto.getEmail())
              .nickname(dto.getNickname())
              .profileImage(dto.getProfileImage())
              .currentWeight(dto.getCurrentWeight())
              .goalWeight(dto.getGoalWeight())
              .startFasting(dto.getStartFasting())
              .endFasting(dto.getEndFasting())
              .build();
    }

    public static ChangeUserInfoRequestDto changeUserInfoRequestDto(String username,
          ChangeUserInfoRequest request) {
        return ChangeUserInfoRequestDto.builder()
              .username(username)
              .nickname(request.getNickname())
              .profileImage(request.getProfileImage())
              .currentWeight(request.getCurrentWeight())
              .goalWeight(request.getGoalWeight())
              .startFasting(request.getStartFasting())
              .endFasting(request.getEndFasting())
              .kcal(request.getKcal())
              .carbs(request.getCarbs())
              .protein(request.getProtein())
              .fat(request.getFat())
              .build();
    }

    public static ChangeUserPasswordRequestDto changeUserPasswordRequestDto(String username,
          ChangeUserPasswordRequest request) {
        return new ChangeUserPasswordRequestDto(username, request.getPassword(),
              request.getChangePassword(), request.getCheckPassword());
    }

    public static UserInfoResponseDto userInfoResponseDto(User user) {
        return new UserInfoResponseDto(user);
    }

    public static SendEmailRequestDto sendEmailRequestDto(String username, String redirectUrl,
          String sendingEmail, EmailType emailType) {
        return new SendEmailRequestDto(
              username,
              redirectUrl,
              sendingEmail,
              emailType
        );
    }

    public static UserNutritionGoalResponseDto userNutritionGoalResponseDto(User user) {
        return new UserNutritionGoalResponseDto(user);
    }

    public static EmailAuthRequestDto emailAuthRequestDto(String username, String authKey) {
        return EmailAuthRequestDto.builder()
              .username(username)
              .authKey(authKey)
              .build();
    }

    public static FindUsernameRequestDto findUsernameRequestDto(String redirectUrl, String email) {
        return new FindUsernameRequestDto(redirectUrl, email);
    }

    public static FindPasswordRequestDto findPasswordRequestDto(String username, String redirectUrl,
          String email) {
        return new FindPasswordRequestDto(username, redirectUrl, email);
    }

    public static ChangeNutritionRequestDto changeNutritionRequestDto(String username,
          ChangeNutritionRequest request) {
        return new ChangeNutritionRequestDto(username,
              request.getKcal(),
              request.getCarbs(),
              request.getProtein(),
              request.getFat());
    }

    public static ChangeFastingTimeRequestDto changeFastingTimeRequestDto(String username, ChangeFastingTimeRequest request) {
        return new ChangeFastingTimeRequestDto(username,
              request.getStartFasting(),
              request.getEndFasting());
    }

    public static CheckDuplicateSignupInputDto checkDuplicateSignupInput(String key, String value) {
        return new CheckDuplicateSignupInputDto(key, value);
    }
}
