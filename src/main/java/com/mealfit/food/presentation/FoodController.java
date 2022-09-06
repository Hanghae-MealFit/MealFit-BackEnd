package com.mealfit.food.presentation;


import com.mealfit.food.application.dto.FoodServiceDtoFactory;
import com.mealfit.food.application.dto.request.CreateFoodRequestDto;
import com.mealfit.food.presentation.dto.request.CreateFoodRequest;
import com.mealfit.food.presentation.dto.response.FoodInfoResponse;
import com.mealfit.food.application.FoodService;
import com.mealfit.config.security.details.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    // 음식 검색
    @GetMapping
    public ResponseEntity<List<FoodInfoResponse>> getFood(@RequestParam("name") String food) {
        return ResponseEntity.status(HttpStatus.OK)
              .body(foodService.getFood(food));
    }

    // 음식 입력
    @PostMapping
    public ResponseEntity<String> createFood(@RequestBody CreateFoodRequest request,
          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        CreateFoodRequestDto requestDto =
              FoodServiceDtoFactory.createFoodRequestDto(request,
                    userDetailsImpl.getUser().getId());

        foodService.createFood(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
              .body("음식 입력 완료");
    }

}
