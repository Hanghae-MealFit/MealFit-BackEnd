package com.mealfit.user.service.dto.request;


import java.io.Serializable;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
public class ChangeUserInfoRequestDto implements Serializable {

    private String username;
    private String nickname;
    private MultipartFile profileImage;
    private double currentWeight;
    private double goalWeight;
    private LocalTime startFasting;
    private LocalTime endFasting;
    private double kcal;
    private double carbs;
    private double protein;
    private double fat;

    @Builder
    public ChangeUserInfoRequestDto(String username, String nickname, MultipartFile profileImage,
          double currentWeight, double goalWeight, LocalTime startFasting, LocalTime endFasting,
          double kcal, double carbs, double protein, double fat) {
        this.username = username;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.currentWeight = currentWeight;
        this.goalWeight = goalWeight;
        this.startFasting = startFasting;
        this.endFasting = endFasting;
        this.kcal = kcal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }
}
