package com.mealfit.user.controller.dto;

import com.mealfit.user.controller.dto.response.UserInfoResponse;
import com.mealfit.user.controller.dto.response.UserNutritionGoalResponse;
import com.mealfit.user.service.dto.response.UserInfoResponseDto;
import com.mealfit.user.service.dto.response.UserNutritionGoalResponseDto;

public class UserControllerDtoFactory {

    public static UserInfoResponse userInfoResponse(UserInfoResponseDto dto) {
        return UserInfoResponse.builder()
              .userId(dto.getUserId())
              .email(dto.getEmail())
              .nickname(dto.getNickname())
              .profileImage(dto.getProfileImage())
              .userStatus(dto.getUserStatus())
              .providerType(dto.getProviderType())
              .goalWeight(dto.getGoalWeight())
              .startFasting(dto.getStartFasting())
              .endFasting(dto.getEndFasting())
              .build();
    }

    public static UserNutritionGoalResponse userNutritionGoalResponse(
          UserNutritionGoalResponseDto dto) {
        return UserNutritionGoalResponse.builder()
              .kcal(dto.getKcal())
              .carbs(dto.getCarbs())
              .protein(dto.getProtein())
              .fat(dto.getFat())
              .build();
    }

}
