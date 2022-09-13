package com.mealfit.food.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodRequestDto {

    private String foodName;
    private Pageable pageable;
    private Long lastId;
}
