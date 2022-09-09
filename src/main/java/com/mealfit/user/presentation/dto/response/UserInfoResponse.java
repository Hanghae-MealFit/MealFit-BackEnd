package com.mealfit.user.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mealfit.user.domain.ProviderType;
import com.mealfit.user.domain.UserStatus;
import java.io.Serializable;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoResponse implements Serializable {

    private Long userId;

    @JsonProperty("userProfile")
    private UserProfileResponse userProfile;

    @JsonProperty("fastingInfo")
    private UserFastingResponse fastingInfo;

    @JsonProperty("nutritionGoal")
    private NutritionGoalResponse nutritionGoal;

    @Builder
    public UserInfoResponse(Long userId, String nickname, String profileImage,
          double goalWeight, UserStatus userStatus, ProviderType providerType, LocalTime startFasting, LocalTime endFasting,
          double kcal, double carbs, double protein, double fat) {
        this.userId = userId;
        this.userProfile = new UserProfileResponse(nickname, profileImage,
              goalWeight, userStatus, providerType);
        this.fastingInfo = new UserFastingResponse(startFasting, endFasting);
        this.nutritionGoal = new NutritionGoalResponse(kcal, carbs, protein, fat);
    }

    @Data
    public static class UserProfileResponse  implements Serializable {

        private String nickname;
        private String profileImage;
        private double goalWeight;
        private String userStatus;
        private String providerType;

        public UserProfileResponse(String nickname, String profileImage,
              double goalWeight, UserStatus userStatus, ProviderType providerType) {

            this.nickname = nickname;
            this.profileImage = profileImage;
            this.goalWeight = goalWeight;
            this.userStatus = userStatus.name();
            this.providerType = providerType.name();
        }
    }

    @Data
    public static class UserFastingResponse  implements Serializable {

        private LocalTime startFasting;
        private LocalTime endFasting;

        public UserFastingResponse(LocalTime startFasting, LocalTime endFasting) {
            this.startFasting = startFasting;
            this.endFasting = endFasting;
        }
    }

    @Data
    public static class NutritionGoalResponse  implements Serializable {

        private double kcal;
        private double carbs;
        private double protein;
        private double fat;

        public NutritionGoalResponse(double kcal, double carbs, double protein, double fat) {
            this.kcal = kcal;
            this.carbs = carbs;
            this.protein = protein;
            this.fat = fat;
        }
    }
}
