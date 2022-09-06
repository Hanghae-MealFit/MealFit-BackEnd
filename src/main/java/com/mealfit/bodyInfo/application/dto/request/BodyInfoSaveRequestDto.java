package com.mealfit.bodyInfo.application.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BodyInfoSaveRequestDto {

    private Long userId;
    private double weight;
    private LocalDate savedDate;

}
