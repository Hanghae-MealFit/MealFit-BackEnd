package com.mealfit.bodyInfo.application.dto.request;

import lombok.Getter;

@Getter
public class BodyInfoDeleteRequestDto {

    private Long userId;
    private Long bodyInfoId;

    public BodyInfoDeleteRequestDto(Long userId, Long bodyInfoId) {
        this.userId = userId;
        this.bodyInfoId = bodyInfoId;
    }
}
