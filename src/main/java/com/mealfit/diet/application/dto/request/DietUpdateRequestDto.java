package com.mealfit.diet.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DietUpdateRequestDto { // 업데이트 용으로 다시 써야지

    private Long userId;
    private Long dietId;  // 식단 ID
    private Long foodId; // 음식 ID,
    private double foodWeight; // 음식 중량

    public DietUpdateRequestDto(Long userId, Long dietId, Long foodId, double foodWeight) {
        this.userId = userId;
        this.dietId = dietId;
        this.foodId = foodId;
        this.foodWeight = foodWeight;
    }
}
