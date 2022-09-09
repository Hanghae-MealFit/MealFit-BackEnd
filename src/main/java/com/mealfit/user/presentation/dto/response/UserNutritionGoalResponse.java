package com.mealfit.user.presentation.dto.response;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNutritionGoalResponse implements Serializable {

    private double kcal;

    private double carbs;

    private double protein;

    private double fat;

    @Builder
    public UserNutritionGoalResponse(double kcal, double carbs, double protein, double fat) {
        this.kcal = kcal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }
}
