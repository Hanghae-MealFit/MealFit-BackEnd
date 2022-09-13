package com.mealfit.food.application.dto;

import com.mealfit.food.application.dto.request.FoodRequestDto;
import com.mealfit.food.presentation.dto.request.FoodSaveRequest;
import com.mealfit.food.application.dto.request.FoodSaveRequestDto;
import org.springframework.data.domain.Pageable;

public class FoodServiceDtoFactory {

    public static FoodSaveRequestDto foodSaveRequestDto(FoodSaveRequest request, Long userId) {
        return new FoodSaveRequestDto(
              request.getFoodName(),
              request.getOneServing(),
              request.getKcal(),
              request.getCarbs(),
              request.getProtein(),
              request.getFat(),
              request.getMadeBy());
    }

    public static FoodRequestDto foodRequestDto(String foodName, Pageable pageable, long lastId) {
        return new FoodRequestDto(
              foodName,
              pageable,
              lastId
        );
    }

}
