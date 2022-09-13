package com.mealfit.food.presentation.dto.request;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FoodSaveRequest implements Serializable {   // 회원이 음식을 직접 입력

    @NotBlank
    private String foodName;
    @NotNull
    private double oneServing;
    @NotNull
    private double kcal;
    @NotNull
    private double carbs;
    @NotNull
    private double protein;
    @NotNull
    private double fat;
    @NotNull
    private String madeBy;

    public FoodSaveRequest(String foodName, double oneServing, double kcal, double carbs,
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
