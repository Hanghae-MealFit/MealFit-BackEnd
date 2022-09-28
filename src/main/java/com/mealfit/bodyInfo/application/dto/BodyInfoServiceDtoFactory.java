package com.mealfit.bodyInfo.application.dto;

import com.mealfit.bodyInfo.application.dto.request.BodyInfoChangeRequestDto;
import com.mealfit.bodyInfo.application.dto.request.BodyInfoDeleteRequestDto;
import com.mealfit.bodyInfo.application.dto.request.BodyInfoRequestDto;
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
              request.getSavedDate()
        );
    }

    public static BodyInfoRequestDto bodyInfoRequestDto(Long userId) {
        return new BodyInfoRequestDto(userId);
    }

    public static BodyInfoRequestDto bodyInfoRequestDto(Long userId, Long bodyInfoId) {
        return new BodyInfoRequestDto(userId, bodyInfoId);
    }

    public static BodyInfoDeleteRequestDto bodyInfoDeleteRequestDto(Long bodyInfoId, Long userId) {
        return new BodyInfoDeleteRequestDto(bodyInfoId, userId);
    }
}
