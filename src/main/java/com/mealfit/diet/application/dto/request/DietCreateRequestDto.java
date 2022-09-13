package com.mealfit.diet.application.dto.request;

import com.mealfit.diet.domain.Diet;
import com.mealfit.diet.domain.DietStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DietCreateRequestDto {

    private Long userId;
    private Long foodId;  // 음식 ID
    private double foodWeight; // 음식 중량
    private DietStatus status; // 아침 점심 저녁
    private LocalDate date; // 기록 일자

    public Diet toEntity() {
        return new Diet(foodId, status, foodWeight, date);
    }
}
