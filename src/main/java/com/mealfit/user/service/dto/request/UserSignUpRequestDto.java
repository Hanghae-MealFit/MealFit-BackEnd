package com.mealfit.user.service.dto.request;

import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserBasicProfile;
import java.io.Serializable;
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
public class UserSignUpRequestDto implements Serializable {

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
              UserBasicProfile.builder()
                    .username(username)
                    .password(password)
                    .nickname(nickname)
                    .email(email)
                    .goalWeight(goalWeight)
                    .startFasting(startFasting)
                    .endFasting(endFasting)
                    .build());
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
}
