package com.mealfit.common.factory;

import com.mealfit.user.application.dto.request.ChangeNutritionRequestDto;
import com.mealfit.user.application.dto.request.ChangeUserInfoRequestDto;
import com.mealfit.user.application.dto.request.ChangeUserPasswordRequestDto;
import com.mealfit.user.application.dto.request.UserSignUpRequestDto;
import com.mealfit.user.application.dto.response.UserInfoResponseDto;
import com.mealfit.user.domain.ProviderType;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserStatus;
import com.mealfit.user.presentation.dto.request.ChangeUserInfoRequest;
import com.mealfit.user.presentation.dto.request.ChangeUserPasswordRequest;
import com.mealfit.user.presentation.dto.request.UserSignUpRequest;
import com.mealfit.user.presentation.dto.response.UserNutritionGoalResponse;
import java.time.LocalTime;
import org.springframework.web.multipart.MultipartFile;

public class UserFactory {

    private UserFactory() {
    }

    public static User basicUser(Long id, String username) {
        return MockUser.builder()
              .id(id)
              .username(username)
              .build();
    }

    public static User basicUser(Long id, String username, String nickname, String profileImage) {
        return MockUser.builder()
              .id(id)
              .username(username)
              .nickname(nickname)
              .profileImage(profileImage)
              .build();
    }

    public static User mockUser(String username, String password,
          String nickname, String email, String imageUrl,
          double goalWeight, LocalTime startFasting, LocalTime endFasting,
          double kcal, double carbs, double proteins, double fats,
          UserStatus userStatus) {
        return MockUser.builder()
              .username(username)
              .password(password)
              .nickname(nickname)
              .email(email)
              .goalWeight(goalWeight)
              .startFasting(startFasting)
              .endFasting(endFasting)
              .profileImage(imageUrl)
              .kcal(kcal)
              .carbs(carbs)
              .protein(proteins)
              .fat(fats)
              .providerType(ProviderType.LOCAL)
              .userStatus(userStatus)
              .build();
    }

    public static User mockSocialUser(String username,
          String nickname, String email,
          ProviderType providerType, UserStatus userStatus) {
        return MockUser.builder()
              .username(username)
              .nickname(nickname)
              .email(email)
              .providerType(providerType)
              .userStatus(userStatus)
              .build();
    }

    public static UserSignUpRequest mockSignUpRequest(String username, String email,
          String password, String passwordCheck,
          String nickname) {
        return UserSignUpRequest.builder()
              .username(username)
              .email(email)
              .password(password)
              .passwordCheck(passwordCheck)
              .nickname(nickname)
              .build();
    }

    public static UserSignUpRequestDto mockSignUpRequestDto(String username, String email,
          String password, String passwordCheck,
          String nickname, double currentWeight, double goalWeight,
          LocalTime startFasting, LocalTime endFasting) {
        return UserSignUpRequestDto.builder()
              .username(username)
              .email(email)
              .password(password)
              .passwordCheck(passwordCheck)
              .nickname(nickname)
              .currentWeight(currentWeight)
              .goalWeight(goalWeight)
              .startFasting(startFasting)
              .endFasting(endFasting)
              .build();
    }

    public static ChangeUserInfoRequest mockChangeUserInfoRequest(String nickname,
          MultipartFile profileImage, double currentWeight, double goalWeight,
          LocalTime startFasting, LocalTime endFasting) {
        return ChangeUserInfoRequest.builder()
              .nickname(nickname)
              .profileImage(profileImage)
              .currentWeight(currentWeight)
              .goalWeight(goalWeight)
              .startFasting(startFasting)
              .endFasting(endFasting)
              .build();
    }

    public static ChangeUserInfoRequestDto mockChangeUserInfoRequestDto(String username, String nickname,
          MultipartFile profileImage, double currentWeight, double goalWeight,
          LocalTime startFasting, LocalTime endFasting,
          double kcal, double carbs, double protein, double fat) {
        return ChangeUserInfoRequestDto.builder()
              .username(username)
              .nickname(nickname)
              .profileImage(profileImage)
              .currentWeight(currentWeight)
              .goalWeight(goalWeight)
              .startFasting(startFasting)
              .endFasting(endFasting)
              .kcal(kcal)
              .carbs(carbs)
              .protein(protein)
              .fat(fat)
              .build();
    }

    public static ChangeUserPasswordRequest mockChangeUserPasswordRequest(String password,
          String changePassword, String passwordCheck) {
        return ChangeUserPasswordRequest.builder()
              .password(password)
              .changePassword(changePassword)
              .checkPassword(passwordCheck)
              .build();
    }

    public static ChangeUserPasswordRequestDto mockChangeUserPasswordRequestDto(String username,
          String password, String changePassword, String checkPassword) {
        return ChangeUserPasswordRequestDto.builder()
              .username(username)
              .password(password)
              .changePassword(changePassword)
              .checkPassword(checkPassword)
              .build();
    }

    public static ChangeNutritionRequestDto mockNutritionRequestDto(String username,
          double kcal, double carbs, double proteins, double fats) {
        return new ChangeNutritionRequestDto(username, kcal, carbs, proteins, fats);
    }

    public static UserInfoResponseDto mockUserInfoResponseDto(Long userId, String username,
          String nickname, String profileImage, String email,
          double goalWeight, LocalTime startFasting, LocalTime endFasting,
          double kcal, double carbs, double protein, double fat,
          UserStatus userStatus, ProviderType providerType) {
        return UserInfoResponseDto.builder()
              .userId(userId)
              .username(username)
              .nickname(nickname)
              .email(email)
              .profileImage(profileImage)
              .goalWeight(goalWeight)
              .startFasting(startFasting)
              .endFasting(endFasting)
              .kcal(kcal)
              .carbs(carbs)
              .protein(protein)
              .fat(fat)
              .userStatus(userStatus)
              .providerType(providerType)
              .build();
    }

    public static UserNutritionGoalResponse mockUserNutritionGoalResponseDto(double kcal,
          double carbs, double protein, double fat) {
        return new UserNutritionGoalResponse(kcal, carbs, protein, fat);
    }
}
