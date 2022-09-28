package com.mealfit.unit.food.application;

import static java.util.stream.Collectors.toList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mealfit.food.application.FoodService;
import com.mealfit.food.application.dto.request.FoodRequestDto;
import com.mealfit.food.application.dto.request.FoodSaveRequestDto;
import com.mealfit.food.domain.Food;
import com.mealfit.food.domain.FoodRepository;
import com.mealfit.food.presentation.dto.response.FoodInfoResponse;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class FoodServiceTest {

    @InjectMocks
    private FoodService foodService;

    @Mock
    private FoodRepository foodRepository;

    @DisplayName("saveFood() 메서드는")
    @Nested
    class Testing_saveFood {

        @DisplayName("FoodSaveRequestDto가 완전히 입력되면 저장에 성공한다.")
        @Test
        void saveFood_success() {
            FoodSaveRequestDto requestDto = new FoodSaveRequestDto("사과", 100,
                  100, 20, 1, 1, "전국");

            foodService.saveFood(requestDto);
        }
    }

    @DisplayName("getFood() 메서드는")
    @Nested
    class Testing_getFood {

        @DisplayName("FoodRequestDto가 완전히 입력되면 저장에 성공한다.")
        @Test
        void getFood_success() {

            // given
            FoodRequestDto requestDto = new FoodRequestDto("사과", Pageable.ofSize(6), 10L);

            List<Food> foods = List.of(
                  new Food(1L, "사과", 100,
                        100, 10, 1, 1, "전국"),
                  new Food(2L, "사과잼", 50,
                        200, 30, 1, 1, "전국"),
                  new Food(3L, "사과파이", 100,
                        270, 20, 1, 1, "전국")
            );

            given(foodRepository.findByIdLessThanAndFoodNameContaining(anyLong(), anyString(),
                  any(Pageable.class))).willReturn(foods);

            // when
            List<FoodInfoResponse> result = foodService.getFood(requestDto);

            // then
            Assertions.assertThat(result).usingRecursiveComparison()
                  .isEqualTo(foods.stream()
                        .map(FoodInfoResponse::new)
                        .collect(toList()));

            verify(foodRepository, times(1))
                  .findByIdLessThanAndFoodNameContaining(anyLong(), anyString(),
                        any(Pageable.class));
        }
    }
}
