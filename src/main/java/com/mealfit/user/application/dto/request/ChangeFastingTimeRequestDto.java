package com.mealfit.user.application.dto.request;

import java.time.LocalTime;
import lombok.Getter;

@Getter
public class ChangeFastingTimeRequestDto {

    private Long userId;
    private LocalTime startFasting;
    private LocalTime endFasting;

    public ChangeFastingTimeRequestDto(Long userId, LocalTime startFasting,
          LocalTime endFasting) {
        this.userId = userId;
        this.startFasting = startFasting;
        this.endFasting = endFasting;
    }
}
