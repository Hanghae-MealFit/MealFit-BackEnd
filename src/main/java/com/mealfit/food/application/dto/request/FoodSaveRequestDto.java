package com.mealfit.food.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class FoodSaveRequestDto {   // 회원이 음식을 직접 입력

    private String foodName; // 음식 이름

    private double oneServing; // 1회 제공량

    private double kcal; // 칼로리

    private double carbs; // 탄수화물

    private double protein; // 단백질

    private double fat; // 지방

    private String madeBy;

    public FoodSaveRequestDto(String foodName, double oneServing, double kcal, double carbs,
          double protein, double fat, String madeBy) {
        this.foodName = foodName;
        this.oneServing = oneServing;
        this.kcal = kcal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
        this.madeBy = madeBy;
    }
}
