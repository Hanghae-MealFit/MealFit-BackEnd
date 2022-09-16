package com.mealfit.bodyInfo.presentation.dto.request;

import lombok.Getter;

@Getter
public class BodyInfoDeleteRequestDto {

    private Long bodyInfoId;
    private Long userId;

    public BodyInfoDeleteRequestDto(Long bodyInfoId, Long userId) {
        this.bodyInfoId = bodyInfoId;
        this.userId = userId;
    }
}
