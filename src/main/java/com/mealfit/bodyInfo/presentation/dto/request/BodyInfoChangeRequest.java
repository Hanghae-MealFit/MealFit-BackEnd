package com.mealfit.bodyInfo.presentation.dto.request;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BodyInfoChangeRequest implements Serializable {

    @NotNull(message = "ID 값 누락")
    private Long id;

    @Max(value = 0)
    @NotNull(message = "몸무게를 입력해주세요.")
    private double weight;

    @Min(value = 0)
    @NotNull(message = "체지방을 입력해주세요.")
    private double bodyFat;

    @NotNull(message = "체지방을 입력해주세요.")
    private LocalDate saveDate;
}
