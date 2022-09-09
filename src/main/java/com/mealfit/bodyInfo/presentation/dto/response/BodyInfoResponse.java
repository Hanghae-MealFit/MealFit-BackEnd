package com.mealfit.bodyInfo.presentation.dto.response;

import com.mealfit.bodyInfo.domain.BodyInfo;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

@Data
public class BodyInfoResponse implements Serializable {

    private Long id;
    private double weight;
    private LocalDate savedDate;

    public BodyInfoResponse(BodyInfo bodyInfo) {
        this.id = bodyInfo.getId();
        this.weight = bodyInfo.getWeight();
        this.savedDate = bodyInfo.getSavedDate();
    }
}
