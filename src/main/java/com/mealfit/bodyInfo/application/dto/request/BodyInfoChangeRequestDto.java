package com.mealfit.bodyInfo.application.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BodyInfoChangeRequestDto {

    private Long userId;
    private Long id;
    private double weight;
    private LocalDate saveDate;
}
