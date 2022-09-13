package com.mealfit.diet.application.dto;

import com.mealfit.diet.application.dto.request.DietDeleteRequestDto;
import com.mealfit.diet.application.dto.request.DietCreateRequestDto;
import com.mealfit.diet.application.dto.request.DietListByDateRequestDto;
import com.mealfit.diet.application.dto.request.DietUpdateRequestDto;
import com.mealfit.diet.domain.DietStatus;
import com.mealfit.diet.presentation.dto.request.DietSaveRequest;
import com.mealfit.diet.presentation.dto.request.DietUpdateRequest;
import java.time.LocalDate;

public class DietServiceDtoFactory {

    public static DietCreateRequestDto dietCreateRequestDto(Long userId, DietSaveRequest request) {
        return new DietCreateRequestDto(userId,
              request.getFoodId(),
              request.getFoodWeight(),
              DietStatus.valueOf(request.getStatus()),
              request.getDate());
    }

    public static DietListByDateRequestDto dietListByDateRequestDto(Long userId,
          LocalDate localDate) {
        return new DietListByDateRequestDto(userId, localDate);
    }

    public static DietUpdateRequestDto dietChangeRequestDto(Long userId, DietUpdateRequest request) {
        return new DietUpdateRequestDto(userId,
              request.getDietId(),
              request.getFoodId(),
              request.getFoodWeight());
    }

    public static DietDeleteRequestDto dietDeleteRequestDto(Long dietId, Long userId) {
        return new DietDeleteRequestDto(dietId, userId);
    }
}
