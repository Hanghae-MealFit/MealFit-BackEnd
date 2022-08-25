package com.mealfit.user.service.dto;

import com.mealfit.user.controller.dto.request.ChangeUserInfoRequest;
import com.mealfit.user.controller.dto.request.ChangeUserPasswordRequest;
import com.mealfit.user.controller.dto.request.UserSignUpRequest;
import com.mealfit.user.domain.User;
import com.mealfit.user.service.dto.request.ChangeUserInfoRequestDto;
import com.mealfit.user.service.dto.request.ChangeUserPasswordRequestDto;
import com.mealfit.user.service.dto.request.UserSignUpRequestDto;
import com.mealfit.user.service.dto.response.UserInfoResponseDto;
import com.mealfit.user.service.dto.response.UserNutritionGoalResponseDto;

public class UserServiceDtoFactory {

    public static UserSignUpRequestDto userSignUpRequestDto(String redirectUrl, UserSignUpRequest dto) {
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

    public static ChangeUserInfoRequestDto ChangeUserInfoRequestDto(String username,
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
              request.getPasswordCheck());
    }

    public static UserInfoResponseDto userInfoResponseDto(User user) {
        return new UserInfoResponseDto(user);
    }

    public static UserNutritionGoalResponseDto userNutritionGoalResponseDto(User user) {
        return new UserNutritionGoalResponseDto(user);
    }

}
