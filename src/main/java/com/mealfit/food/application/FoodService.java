package com.mealfit.food.application;

import static java.util.stream.Collectors.toList;

import com.mealfit.food.application.dto.request.FoodRequestDto;
import com.mealfit.food.application.dto.request.FoodSaveRequestDto;
import com.mealfit.food.domain.Food;
import com.mealfit.food.domain.FoodRepository;
import com.mealfit.food.presentation.dto.response.FoodInfoResponse;
import java.util.List;
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
    @Transactional(readOnly = true)
    public List<FoodInfoResponse> getFood(FoodRequestDto requestDto) {

        List<Food> foodPage = foodRepository.findByIdLessThanAndFoodNameContaining(
              requestDto.getLastId(),
              requestDto.getFoodName(),
              requestDto.getPageable());

        return foodPage.stream()
              // 람다식 -> 메서드 참조 (method Reference)
              .map(FoodInfoResponse::new)        //(타입 -> 다른 타입으로 변경)
              .collect(toList());    // List에 담아주기
    }

    // 음식 입력
    @Transactional
    public void saveFood(FoodSaveRequestDto dto) {

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
