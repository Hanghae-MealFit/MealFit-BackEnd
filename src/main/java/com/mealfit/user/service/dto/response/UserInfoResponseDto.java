package com.mealfit.user.service.dto.response;

import com.mealfit.user.domain.ProviderType;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserStatus;
import java.io.Serializable;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoResponseDto implements Serializable {

    private Long userId;
    private String email;
    private String nickname;
    private String profileImage;
    private double goalWeight;
    private UserStatus userStatus;
    private ProviderType providerType;
    private LocalTime startFasting;
    private LocalTime endFasting;

    public UserInfoResponseDto(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
        this.goalWeight = user.getGoalWeight();
        this.userStatus = user.getUserStatus();
        this.providerType = user.getProviderType();
        this.startFasting = user.getStartFasting();
        this.endFasting = user.getEndFasting();
    }

    @Builder
    public UserInfoResponseDto(Long userId, String email, String nickname, String profileImage,
          double goalWeight, UserStatus userStatus, ProviderType providerType,
          LocalTime startFasting,
          LocalTime endFasting) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.goalWeight = goalWeight;
        this.userStatus = userStatus;
        this.providerType = providerType;
        this.startFasting = startFasting;
        this.endFasting = endFasting;
    }
}
