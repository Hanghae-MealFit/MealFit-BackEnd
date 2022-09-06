package com.mealfit.user.application.dto.request;

import com.mealfit.user.domain.FastingTime;
import com.mealfit.user.domain.LoginInfo;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserProfile;
import com.mealfit.user.presentation.dto.request.UserSignUpRequest;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSignUpRequestDto {

    private String redirectURL;
    private String username;
    private String password;
    private String passwordCheck;
    private String email;
    private String nickname;
    private MultipartFile profileImage;
    private double currentWeight;
    private double goalWeight;
    private LocalTime startFasting;
    private LocalTime endFasting;

    public User toEntity() {
        return User.createLocalUser(
              new LoginInfo(username, password),
              new UserProfile(nickname, email, null),
              goalWeight,
              new FastingTime(startFasting, endFasting), null);
    }

    @Builder
    public UserSignUpRequestDto(String redirectURL, String username, String password,
          String passwordCheck, String email, String nickname, MultipartFile profileImage,
          double currentWeight, double goalWeight, LocalTime startFasting, LocalTime endFasting) {
        this.redirectURL = redirectURL;
        this.username = username;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.currentWeight = currentWeight;
        this.goalWeight = goalWeight;
        this.startFasting = startFasting;
        this.endFasting = endFasting;
    }

    public UserSignUpRequestDto(String redirectURL, UserSignUpRequest request) {
        this.redirectURL = redirectURL;
        this.username = request.getUsername();
        this.password = request.getPassword();
        this.passwordCheck = request.getPasswordCheck();
        this.email = request.getEmail();
        this.nickname = request.getNickname();
        this.profileImage = request.getProfileImage();
        this.currentWeight = request.getCurrentWeight();
        this.goalWeight = request.getGoalWeight();
        this.startFasting = request.getStartFasting();
        this.endFasting = request.getEndFasting();
    }
}
