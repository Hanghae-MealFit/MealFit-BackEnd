package com.mealfit.food.application;

import com.mealfit.food.application.dto.request.CreateFoodRequestDto;
import com.mealfit.food.domain.Food;
import com.mealfit.food.domain.FoodRepository;
import com.mealfit.food.presentation.dto.response.FoodInfoResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FoodService {

    private final FoodRepository foodRepository;

    // 음식 검색
    public List<FoodInfoResponse> getFood(String foodName) {

        List<Food> searchFood = foodRepository.findByFoodNameContaining(foodName);

        return searchFood.stream()
              // 람다식 -> 메서드 참조 (method Reference)
              .map(FoodInfoResponse::new)        //(타입 -> 다른 타입으로 변경)
              .collect(Collectors.toList());    // List에 담아주기
    }

    // 음식 입력
    @Transactional
    public void createFood(CreateFoodRequestDto dto) {

        Food food = Food.builder()
              .foodName(dto.getFoodName())
              .oneServing(dto.getOneServing())
              .kcal(dto.getKcal())
              .carbs(dto.getCarbs())
              .protein(dto.getProtein())
              .fat(dto.getFat())
              .madeBy(dto.getMadeBy())
              .build();
        foodRepository.save(food);
    }
}
