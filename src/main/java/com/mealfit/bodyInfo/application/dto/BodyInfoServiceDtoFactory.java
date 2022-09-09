package com.mealfit.bodyInfo.application.dto;

import com.mealfit.bodyInfo.application.dto.request.BodyInfoChangeRequestDto;
import com.mealfit.bodyInfo.application.dto.request.BodyInfoSaveRequestDto;
import com.mealfit.bodyInfo.presentation.dto.request.BodyInfoChangeRequest;
import com.mealfit.bodyInfo.presentation.dto.request.BodyInfoSaveRequest;

public class BodyInfoServiceDtoFactory {

    public static BodyInfoSaveRequestDto bodyInfoSaveRequestDto(BodyInfoSaveRequest request, Long userId) {
        return new BodyInfoSaveRequestDto(
              userId,
              request.getWeight(),
              request.getSavedDate()
        );
    }

    public static BodyInfoChangeRequestDto bodyInfoChangeRequestDto(BodyInfoChangeRequest request, Long userId) {
        return new BodyInfoChangeRequestDto(
              userId,
              request.getId(),
              request.getWeight(),
              request.getSaveDate()
        );
    }
}
