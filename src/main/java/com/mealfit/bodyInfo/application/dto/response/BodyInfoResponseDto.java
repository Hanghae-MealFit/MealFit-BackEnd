package com.mealfit.bodyInfo.application.dto.response;

import com.mealfit.bodyInfo.domain.BodyInfo;
import java.time.LocalDate;
import lombok.Data;

@Data
public class BodyInfoResponseDto {

    private Long id;
    private double weight;
    private LocalDate savedDate;

    public BodyInfoResponseDto(BodyInfo bodyInfo) {
        this.id = bodyInfo.getId();
        this.weight = bodyInfo.getWeight();
        this.savedDate = bodyInfo.getSavedDate();
    }
}
