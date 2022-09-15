package com.mealfit.user.presentation.dto.request;

import java.io.Serializable;
import javax.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangeNutritionRequest implements Serializable {

    @Min(value = 0, message = "0보다 크게 입력해주세요")
    private double kcal;

    @Min(value = 0, message = "0보다 크게 입력해주세요")
    private double carbs;

    @Min(value = 0, message = "0보다 크게 입력해주세요")
    private double protein;

    @Min(value = 0, message = "0보다 크게 입력해주세요")
    private double fat;

    public ChangeNutritionRequest(double kcal, double carbs, double protein, double fat) {
        this.kcal = kcal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }
}
