package com.mealfit.diet.application.dto.request;

import lombok.Getter;

@Getter
public class DietDeleteRequestDto {

    private Long dietId;
    private Long userId;

    public DietDeleteRequestDto(Long dietId, Long userId) {
        this.dietId = dietId;
        this.userId = userId;
    }
}
