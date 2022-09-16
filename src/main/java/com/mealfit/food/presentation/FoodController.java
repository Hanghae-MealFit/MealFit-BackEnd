package com.mealfit.food.presentation;


import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.food.application.FoodService;
import com.mealfit.food.application.dto.FoodServiceDtoFactory;
import com.mealfit.food.application.dto.request.FoodRequestDto;
import com.mealfit.food.application.dto.request.FoodSaveRequestDto;
import com.mealfit.food.presentation.dto.request.FoodSaveRequest;
import com.mealfit.food.presentation.dto.response.FoodInfoResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    public static final int DEFAULT_PAGE_SIZE = 6;

    // 음식 검색
    @GetMapping
    public ResponseEntity<List<FoodInfoResponse>> getFood(
          @RequestParam("name") String foodName,
          @RequestParam(defaultValue = "" + Long.MAX_VALUE) long lastId,
          @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = DEFAULT_PAGE_SIZE) Pageable pageable) {

        FoodRequestDto requestDto = FoodServiceDtoFactory.foodRequestDto(foodName, pageable, lastId);

        return ResponseEntity.status(HttpStatus.OK)
              .body(foodService.getFood(requestDto));
    }

    // 음식 입력
    @PostMapping
    public ResponseEntity<String> saveFood(@RequestBody FoodSaveRequest request,
          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        FoodSaveRequestDto requestDto = FoodServiceDtoFactory.foodSaveRequestDto(
              request,
              userDetailsImpl.getUser().getId());

        foodService.saveFood(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
              .body("음식 입력 완료");
    }
}
