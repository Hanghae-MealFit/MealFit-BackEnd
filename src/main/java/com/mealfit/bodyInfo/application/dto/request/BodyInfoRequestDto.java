package com.mealfit.bodyInfo.application.dto.request;

import lombok.Getter;

@Getter
public class BodyInfoRequestDto {

    private Long userId;
    private Long bodyInfoId;

    public BodyInfoRequestDto(Long userId) {
        this.userId = userId;
    }

    public BodyInfoRequestDto(Long userId, Long bodyInfoId) {
        this.userId = userId;
        this.bodyInfoId = bodyInfoId;
    }
}
