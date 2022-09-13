package com.mealfit.diet.presentation.dto.request;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class DietUpdateRequest implements Serializable {

    @NotNull
    private Long dietId;  // 식단 ID

    @NotNull
    private Long foodId; // 바꿀 음식ID,

    @NotNull
    private double foodWeight; // 음식 중량

    public DietUpdateRequest(Long dietId, Long foodId, double foodWeight) {
        this.dietId = dietId;
        this.foodId = foodId;
        this.foodWeight = foodWeight;
    }
}
