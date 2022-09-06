package com.mealfit.user.application.dto.response;

import com.mealfit.user.domain.ProviderType;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserStatus;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class UserInfoResponseDto {

    private Long userId;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String profileImage;
    private double goalWeight;
    private UserStatus userStatus;
    private ProviderType providerType;
    private LocalTime startFasting;
    private LocalTime endFasting;
    private double kcal;
    private double carbs;
    private double protein;
    private double fat;

    @Builder
    public UserInfoResponseDto(Long userId, String username, String password, String email,
          String nickname, String profileImage, double goalWeight, UserStatus userStatus,
          ProviderType providerType, LocalTime startFasting, LocalTime endFasting, double kcal,
          double carbs, double protein, double fat) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.goalWeight = goalWeight;
        this.userStatus = userStatus;
        this.providerType = providerType;
        this.startFasting = startFasting;
        this.endFasting = endFasting;
        this.kcal = kcal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }

    public UserInfoResponseDto(User user) {
        this.userId = user.getId();
        this.username = user.getLoginInfo().getUsername();
        this.password = user.getLoginInfo().getPassword();
        this.email = user.getUserProfile().getEmail();
        this.nickname = user.getUserProfile().getNickname();
        this.profileImage = user.getUserProfile().getProfileImage();
        this.goalWeight = user.getGoalWeight();
        this.userStatus = user.getUserStatusInfo().getUserStatus();
        this.providerType = user.getUserStatusInfo().getProviderType();
        this.startFasting = user.getFastingTime().getStartFasting();
        this.endFasting = user.getFastingTime().getEndFasting();
        this.kcal = user.getNutrition().getKcal();
        this.carbs = user.getNutrition().getCarbs();
        this.protein = user.getNutrition().getProtein();
        this.fat = user.getNutrition().getFat();
    }
}
